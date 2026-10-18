package com.daa.algorithms;

import com.daa.graph.Edge;
import com.daa.graph.Graph;

import java.util.Arrays;

public final class DAGShortestPath {

    private DAGShortestPath() {
    }

    public static DAGShortestPathResult findShortestPaths(Graph graph) {
        if (!graph.isDirected()) {
            throw new IllegalArgumentException(
                    "DAG shortest paths require a directed graph"
            );
        }

        int n = graph.getVertexCount();

        // Get a topological order. It also detects cycles.
        KahnResult topologicalResult = Kahn.topologicalSort(graph);

        if (!topologicalResult.isAcyclic()) {
            throw new IllegalArgumentException(
                    "Shortest paths require an acyclic graph"
            );
        }

        long[] distances = new long[n];
        int[] parents = new int[n];

        Arrays.fill(distances, Long.MAX_VALUE);
        Arrays.fill(parents, -1);

        // Every source vertex starts with distance zero.
        int[] inDegree = new int[n];

        for (Edge edge : graph.getEdges()) {
            inDegree[edge.getTo()]++;
        }

        for (int vertex = 0; vertex < n; vertex++) {
            if (inDegree[vertex] == 0) {
                distances[vertex] = 0;
            }
        }

        // Process vertices in topological order.
        for (int vertex : topologicalResult.getOrder()) {
            if (distances[vertex] == Long.MAX_VALUE) {
                continue;
            }

            for (Edge edge : graph.getAdjacentEdges(vertex)) {
                int next = edge.getTo();
                long candidate = distances[vertex] + edge.getWeight();

                if (candidate < distances[next]) {
                    distances[next] = candidate;
                    parents[next] = vertex;
                }
            }
        }

        return new DAGShortestPathResult(distances, parents);
    }
}