
package com.daa.algorithms;

import com.daa.graph.Edge;
import com.daa.graph.Graph;

import java.util.Arrays;

public final class DAGShortestPath {

    private DAGShortestPath() {
    }

    /**
     * Calculates shortest distances from all source vertices
     * (vertices with zero incoming edges).
     */
    public static DAGShortestPathResult findShortestPaths(Graph graph) {
        return findShortestPaths(graph, null);
    }

    public static DAGShortestPathResult findShortestPaths(
            Graph graph,
            Metrics metrics
    ) {
        if (!graph.isDirected()) {
            throw new IllegalArgumentException(
                    "DAG shortest paths require a directed graph"
            );
        }

        int n = graph.getVertexCount();
        int[] inDegree = new int[n];

        for (Edge edge : graph.getEdges()) {
            inDegree[edge.getTo()]++;
        }

        long[] distances = new long[n];
        int[] parents = new int[n];

        Arrays.fill(distances, Long.MAX_VALUE);
        Arrays.fill(parents, -1);

        for (int vertex = 0; vertex < n; vertex++) {
            if (inDegree[vertex] == 0) {
                distances[vertex] = 0;
            }
        }

        return calculate(graph, distances, parents, metrics);
    }

    /**
     * Calculates shortest distances from one specified source vertex.
     * Vertices unreachable from the source retain Long.MAX_VALUE.
     */
    public static DAGShortestPathResult findShortestPaths(
            Graph graph,
            int source
    ) {
        return findShortestPaths(graph, source, null);
    }

    public static DAGShortestPathResult findShortestPaths(
            Graph graph,
            int source,
            Metrics metrics
    ) {
        if (!graph.isDirected()) {
            throw new IllegalArgumentException(
                    "DAG shortest paths require a directed graph"
            );
        }

        int n = graph.getVertexCount();

        if (source < 0 || source >= n) {
            throw new IllegalArgumentException(
                    "Source vertex is out of range"
            );
        }

        long[] distances = new long[n];
        int[] parents = new int[n];

        Arrays.fill(distances, Long.MAX_VALUE);
        Arrays.fill(parents, -1);

        distances[source] = 0;

        return calculate(graph, distances, parents, metrics);
    }

    private static DAGShortestPathResult calculate(
            Graph graph,
            long[] distances,
            int[] parents,
            Metrics metrics
    ) {
        KahnResult topologicalResult =
                Kahn.topologicalSort(graph, metrics);

        if (!topologicalResult.isAcyclic()) {
            throw new IllegalArgumentException(
                    "Shortest paths require an acyclic graph"
            );
        }

        for (int vertex : topologicalResult.getOrder()) {
            if (distances[vertex] == Long.MAX_VALUE) {
                continue;
            }

            for (Edge edge : graph.getAdjacentEdges(vertex)) {
                if (metrics != null) {
                    metrics.recordEdgeScan();
                    metrics.recordComparison();
                }

                int next = edge.getTo();
                long weight = edge.getWeight();

                // Prevent overflow when adding a weight.
                if (weight > 0
                        && distances[vertex] > Long.MAX_VALUE - weight) {
                    continue;
                }

                if (weight < 0
                        && distances[vertex] < Long.MIN_VALUE - weight) {
                    continue;
                }

                long candidate = distances[vertex] + weight;

                if (candidate < distances[next]) {
                    distances[next] = candidate;
                    parents[next] = vertex;
                }
            }
        }

        return new DAGShortestPathResult(distances, parents);
    }
}
