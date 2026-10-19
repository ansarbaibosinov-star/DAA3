package com.daa.benchmark;

import com.daa.algorithms.DAGShortestPath;
import com.daa.algorithms.DAGShortestPathResult;
import com.daa.algorithms.Kahn;
import com.daa.algorithms.KahnResult;
import com.daa.algorithms.Kosaraju;
import com.daa.algorithms.MSTResult;
import com.daa.algorithms.Kruskal;
import com.daa.algorithms.Metrics;
import com.daa.algorithms.Prim;
import com.daa.algorithms.SCCResult;
import com.daa.graph.Graph;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Benchmark {

    private static final int[] SIZES = {
            1000, 3162, 10000, 31623, 100000
    };

    private static final int[] DENSITIES = {4, 20};
    private static final int REPETITIONS = 5;
    private static final long SEED = 42L;

    private Benchmark() {
    }

    public static void main(String[] args) throws IOException {
        Path output = Path.of("results", "benchmark.csv");

        Files.createDirectories(output.getParent());

        List<String> rows = new ArrayList<>();

        rows.add(
                "algorithm,graph,n,m,time_ms,dsu_ops,"
                        + "edge_scans,comparisons,max_depth,result"
        );

        for (int n : SIZES) {
            for (int density : DENSITIES) {
                int m = density * n;

                System.out.println(
                        "Running benchmarks: n=" + n + ", m=" + m
                );

                runMstBenchmarks(n, m, rows);
                runSccBenchmark(n, m, rows);
                runDagBenchmarks(n, m, rows);
            }
        }

        Files.write(
                output,
                rows,
                StandardCharsets.UTF_8
        );

        System.out.println();
        System.out.println("Benchmark completed.");
        System.out.println("CSV saved to: " + output.toAbsolutePath());
        System.out.println("Data rows: " + (rows.size() - 1));
    }

    private static void runMstBenchmarks(
            int n,
            int m,
            List<String> rows
    ) {
        Graph graph = GraphGenerator.generate(
                n, m, GraphGenerator.Type.UNDIRECTED, SEED
        );

        MSTResult kruskalResult = Kruskal.findMST(graph);
        MSTResult primResult = Prim.findMST(graph);

        if (kruskalResult.getTotalWeight()
                != primResult.getTotalWeight()) {
            throw new IllegalStateException(
                    "Kruskal and Prim produced different MST weights: "
                            + kruskalResult.getTotalWeight() + " vs "
                            + primResult.getTotalWeight()
            );
        }

        if (kruskalResult.getComponents()
                != primResult.getComponents()) {
            throw new IllegalStateException(
                    "Kruskal and Prim produced different component counts"
            );
        }

        addRow(
                rows, "Kruskal", "undirected", n, m,
                metrics -> {
                    MSTResult result = Kruskal.findMST(graph, metrics);
                    return result.getTotalWeight()
                            + ";components=" + result.getComponents();
                }
        );

        addRow(
                rows, "Prim", "undirected", n, m,
                metrics -> {
                    MSTResult result = Prim.findMST(graph, metrics);
                    return result.getTotalWeight()
                            + ";components=" + result.getComponents();
                }
        );
    }

    private static void runSccBenchmark(
            int n,
            int m,
            List<String> rows
    ) {
        Graph graph = GraphGenerator.generate(
                n, m, GraphGenerator.Type.DIRECTED, SEED
        );

        addRow(
                rows, "Kosaraju", "directed", n, m,
                metrics -> {
                    SCCResult result = Kosaraju.findSCC(graph, metrics);
                    return Integer.toString(result.getCount());
                }
        );
    }

    private static void runDagBenchmarks(
            int n,
            int m,
            List<String> rows
    ) {
        Graph graph = GraphGenerator.generate(
                n, m, GraphGenerator.Type.DAG, SEED
        );

        addRow(
                rows, "Kahn", "dag", n, m,
                metrics -> {
                    KahnResult result = Kahn.topologicalSort(graph, metrics);

                    if (!result.isAcyclic()) {
                        throw new IllegalStateException(
                                "Generated DAG unexpectedly contains a cycle"
                        );
                    }

                    return Integer.toString(result.getOrder().length);
                }
        );

        addRow(
                rows, "DAGShortestPath", "dag", n, m,
                metrics -> {
                    DAGShortestPathResult result =
                            DAGShortestPath.findShortestPaths(graph, metrics);

                    long sum = 0;
                    int reachable = 0;

                    for (long distance : result.getDistances()) {
                        if (distance != Long.MAX_VALUE) {
                            sum += distance;
                            reachable++;
                        }
                    }

                    return "sum=" + sum + ";reachable=" + reachable;
                }
        );
    }

    private static void addRow(
            List<String> rows,
            String algorithm,
            String graphType,
            int n,
            int m,
            BenchmarkTask task
    ) {
        List<Long> times = new ArrayList<>();
        List<Long> dsuOps = new ArrayList<>();
        List<Long> edgeScans = new ArrayList<>();
        List<Long> comparisons = new ArrayList<>();
        List<Integer> maxDepths = new ArrayList<>();

        String expectedResult = null;

        // Warm-up run: reduce the effect of initial JVM overhead.
        task.run(new Metrics());

        for (int run = 0; run < REPETITIONS; run++) {
            Metrics metrics = new Metrics();

            long start = System.nanoTime();
            String result = task.run(metrics);
            long end = System.nanoTime();

            if (expectedResult == null) {
                expectedResult = result;
            } else if (!expectedResult.equals(result)) {
                throw new IllegalStateException(
                        algorithm + " returned inconsistent results"
                );
            }

            times.add(end - start);
            dsuOps.add(metrics.getDsuOps());
            edgeScans.add(metrics.getEdgeScans());
            comparisons.add(metrics.getComparisons());
            maxDepths.add(metrics.getMaxDepth());
        }

        double medianTimeMs = medianLong(times) / 1_000_000.0;

        rows.add(
                algorithm + ","
                        + graphType + ","
                        + n + ","
                        + m + ","
                        + String.format(
                        java.util.Locale.US, "%.3f", medianTimeMs
                ) + ","
                        + medianLong(dsuOps) + ","
                        + medianLong(edgeScans) + ","
                        + medianLong(comparisons) + ","
                        + medianInt(maxDepths) + ","
                        + expectedResult
        );

        System.out.println(
                "  " + algorithm + ": median="
                        + String.format(
                        java.util.Locale.US, "%.3f", medianTimeMs
                ) + " ms"
        );
    }

    private static long medianLong(List<Long> values) {
        List<Long> sorted = new ArrayList<>(values);
        Collections.sort(sorted);

        int middle = sorted.size() / 2;

        if (sorted.size() % 2 == 1) {
            return sorted.get(middle);
        }

        return sorted.get(middle - 1) / 2
                + sorted.get(middle) / 2
                + (sorted.get(middle - 1) % 2
                + sorted.get(middle) % 2) / 2;
    }

    private static int medianInt(List<Integer> values) {
        List<Integer> sorted = new ArrayList<>(values);
        Collections.sort(sorted);

        return sorted.get(sorted.size() / 2);
    }

    @FunctionalInterface
    private interface BenchmarkTask {
        String run(Metrics metrics);
    }
}
