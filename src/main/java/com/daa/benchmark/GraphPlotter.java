
package com.daa.benchmark;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class GraphPlotter {

    private GraphPlotter() {
    }

    public static void main(String[] args) throws IOException {
        Path csv = Path.of("results", "benchmark.csv");
        Path output = Path.of("results", "plots");

        if (!Files.exists(csv)) {
            throw new IllegalStateException(
                    "benchmark.csv not found. Run Benchmark.java first."
            );
        }

        Files.createDirectories(output);

        List<String> lines = Files.readAllLines(csv);

        plotMst(lines, output.resolve("mst_comparison.png"));
        plotDirected(lines, output.resolve("directed_algorithms.png"));
        plotDag(lines, output.resolve("dag_algorithms.png"));

        System.out.println("Charts saved to: " + output.toAbsolutePath());
    }

    private static void plotMst(List<String> lines, Path output)
            throws IOException {
        XYSeries kruskal = new XYSeries("Kruskal");
        XYSeries prim = new XYSeries("Prim");

        for (int i = 1; i < lines.size(); i++) {
            String[] c = lines.get(i).split(",", -1);

            if (c.length < 5 || !"undirected".equals(c[1])) {
                continue;
            }

            int n = Integer.parseInt(c[2]);
            double time = Double.parseDouble(c[4]);

            if ("Kruskal".equals(c[0])) {
                kruskal.add(n, time);
            } else if ("Prim".equals(c[0])) {
                prim.add(n, time);
            }
        }

        XYSeriesCollection data = new XYSeriesCollection();
        data.addSeries(kruskal);
        data.addSeries(prim);

        saveChart(
                data,
                "Minimum Spanning Forest: Kruskal vs Prim",
                "Number of vertices",
                "Median time (ms)",
                output
        );
    }

    private static void plotDirected(List<String> lines, Path output)
            throws IOException {
        XYSeries kosaraju = new XYSeries("Kosaraju");

        for (int i = 1; i < lines.size(); i++) {
            String[] c = lines.get(i).split(",", -1);

            if (c.length < 5 || !"directed".equals(c[1])) {
                continue;
            }

            kosaraju.add(
                    Integer.parseInt(c[2]),
                    Double.parseDouble(c[4])
            );
        }

        XYSeriesCollection data = new XYSeriesCollection();
        data.addSeries(kosaraju);

        saveChart(
                data,
                "Strongly Connected Components: Kosaraju",
                "Number of vertices",
                "Median time (ms)",
                output
        );
    }

    private static void plotDag(List<String> lines, Path output)
            throws IOException {
        XYSeries kahn = new XYSeries("Kahn");
        XYSeries shortestPath = new XYSeries("DAG Shortest Path");

        for (int i = 1; i < lines.size(); i++) {
            String[] c = lines.get(i).split(",", -1);

            if (c.length < 5 || !"dag".equals(c[1])) {
                continue;
            }

            int n = Integer.parseInt(c[2]);
            double time = Double.parseDouble(c[4]);

            if ("Kahn".equals(c[0])) {
                kahn.add(n, time);
            } else if ("DAGShortestPath".equals(c[0])) {
                shortestPath.add(n, time);
            }
        }

        XYSeriesCollection data = new XYSeriesCollection();
        data.addSeries(kahn);
        data.addSeries(shortestPath);

        saveChart(
                data,
                "DAG Algorithms Performance",
                "Number of vertices",
                "Median time (ms)",
                output
        );
    }

    private static void saveChart(
            XYSeriesCollection data,
            String title,
            String xLabel,
            String yLabel,
            Path output
    ) throws IOException {
        JFreeChart chart = ChartFactory.createXYLineChart(
                title,
                xLabel,
                yLabel,
                data,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        ChartUtils.saveChartAsPNG(
                output.toFile(),
                chart,
                1200,
                700
        );

        System.out.println("Created: " + output);
    }
}
