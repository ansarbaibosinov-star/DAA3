package com.daa.algorithms;

import com.daa.graph.Edge;
import com.daa.graph.Graph;

import java.util.ArrayDeque;
import java.util.Queue;

public final class Kahn {

    private Kahn() {
    }

    public static KahnResult topologicalSort(Graph graph) {
        return topologicalSort(graph, null);
    }

    public static KahnResult topologicalSort(Graph graph, Metrics metrics) {
        if (!graph.isDirected()) {
            throw new IllegalArgumentException(
                    "Kahn's algorithm requires a directed graph"
            );
        }

        int n = graph.getVertexCount();
        int[] inDegree = new int[n];

        // One pass over edges: O(V + E) overall.
        for (Edge edge : graph.getEdges()) {
            recordEdgeScan(metrics);
            inDegree[edge.getTo()]++;
        }

        Queue<Integer> queue = new ArrayDeque<>();

        for (int vertex = 0; vertex < n; vertex++) {
            recordComparison(metrics);
            if (inDegree[vertex] == 0) {
                queue.offer(vertex);
            }
        }

        int[] order = new int[n];
        int orderSize = 0;

        while (!queue.isEmpty()) {
            int vertex = queue.poll();
            order[orderSize++] = vertex;

            for (Edge edge : graph.getAdjacentEdges(vertex)) {
                recordEdgeScan(metrics);

                int next = edge.getTo();
                inDegree[next]--;

                recordComparison(metrics);
                if (inDegree[next] == 0) {
                    queue.offer(next);
                }
            }
        }

        boolean acyclic = orderSize == n;
        int[] actualOrder = new int[orderSize];
        System.arraycopy(order, 0, actualOrder, 0, orderSize);

        return new KahnResult(actualOrder, acyclic);
    }

    private static void recordEdgeScan(Metrics metrics) {
        if (metrics != null) {
            metrics.recordEdgeScan();
        }
    }

    private static void recordComparison(Metrics metrics) {
        if (metrics != null) {
            metrics.recordComparison();
        }
    }
}