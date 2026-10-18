package com.daa.benchmark;

import com.daa.algorithms.*;
import com.daa.graph.Graph;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Locale;

public final class Benchmark {

    private static final int[] SIZES = {
            1_000, 3_162, 10_000, 31_623, 100_000
    };

    private static final int[] DENSITIES = {4, 20};
    private static final int REPETITIONS = 5;
    private static final long SEED = 42L;

    private Benchmark() {
    }

    public static void main(String[] args) throws IOException {
        Path output = Path.of("results", "benchmark.csv");
        Files.createDirectories(output.getParent());

        try (BufferedWriter writer = Files.newBufferedWriter(output)) {
            writer.write(
                    "algorithm,graph,n,m,time_ms,dsu_ops,edge_scans,"
                            + "comparisons,max_depth,result"
            );
            writer.newLine();

            for (int n : SIZES) {
                for (int density : DENSITIES) {
                    int m = density * n;

                    System.out.printf(
                            "Benchmark: n=%d, m=%d%n", n, m
                    );

                    Graph undirected = GraphGenerator.generate(
                            n, m,
                            GraphGenerator.Type.UNDIRECTED,
                            SEED
                    );

                    writeResult(writer, "Kruskal", "UNDIRECTED",
                            undirected, n, m);
                    writeResult(writer, "Prim", "UNDIRECTED",
                            undirected, n, m);

                    Graph directed = GraphGenerator.generate(
                            n, m,
                            GraphGenerator.Type.DIRECTED,
                            SEED
                    );

                    writeResult(writer, "Kosaraju", "DIRECTED",
                            directed, n, m);

                    Graph dag = GraphGenerator.generate(
                            n, m,
                            GraphGenerator.Type.DAG,
                            SEED
                    );

                    writeResult(writer, "Kahn", "DAG", dag, n, m);
                    writeResult(writer, "DAGShortestPath", "DAG",
                            dag, n, m);
                }
            }
        }

        System.out.println("CSV saved to: " + output.toAbsolutePath());
    }

    private static void writeResult(
            BufferedWriter writer,
            String algorithm,
            String graphType,
            Graph graph,
            int n,
            int m
    ) throws IOException {

        long[] times = new long[REPETITIONS];
        long[] dsuOps = new long[REPETITIONS];
        long[] edgeScans = new long[REPETITIONS];
        long[] comparisons = new long[REPETITIONS];
        long[] maxDepth = new long[REPETITIONS];

        String result = "";

        for (int run = 0; run < REPETITIONS; run++) {
            Metrics metrics = new Metrics();

            long start = System.nanoTime();

            switch (algorithm) {
                case "Kruskal" -> {
                    MSTResult r = Kruskal.findMST(graph, metrics);
                    result = Long.toString(r.getTotalWeight());
                }
                case "Prim" -> {
                    MSTResult r = Prim.findMST(graph, metrics);
                    result = Long.toString(r.getTotalWeight());
                }
                case "Kosaraju" -> {
                    SCCResult r = Kosaraju.findSCC(graph, metrics);
                    result = Integer.toString(r.getComponentCount());
                }
                case "Kahn" -> {
                    KahnResult r = Kahn.topologicalSort(graph, metrics);
                    result = r.isAcyclic()
                            ? Integer.toString(r.getOrder().length)
                            : "CYCLE";
                }
                case "DAGShortestPath" -> {
                    DAGShortestPathResult r =
                            DAGShortestPath.findShortestPaths(graph, metrics);

                    long sum = 0;
                    for (long distance : r.getDistances()) {
                        if (distance != Long.MAX_VALUE) {
                            sum += distance;
                        }
                    }
                    result = Long.toString(sum);
                }
                default -> throw new IllegalArgumentException(
                        "Unknown algorithm: " + algorithm
                );
            }

            times[run] = System.nanoTime() - start;
            dsuOps[run] = metrics.getDsuOps();
            edgeScans[run] = metrics.getEdgeScans();
            comparisons[run] = metrics.getComparisons();
            maxDepth[run] = metrics.getMaxDepth();
        }

        double medianMs = median(times) / 1_000_000.0;

        writer.write(String.format(
                Locale.US,
                "%s,%s,%d,%d,%.3f,%d,%d,%d,%d,%s",
                algorithm,
                graphType,
                n,
                m,
                medianMs,
                median(dsuOps),
                median(edgeScans),
                median(comparisons),
                median(maxDepth),
                result
        ));
        writer.newLine();
        writer.flush();
    }

    private static long median(long[] values) {
        long[] sorted = values.clone();
        Arrays.sort(sorted);
        return sorted[sorted.length / 2];
    }
}