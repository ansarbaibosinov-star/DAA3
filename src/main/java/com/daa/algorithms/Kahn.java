package com.daa.algorithms;

import com.daa.graph.Edge;
import com.daa.graph.Graph;

import java.util.ArrayDeque;
import java.util.Queue;

public final class Kahn {

    private Kahn() {
    }

    public static KahnResult topologicalSort(Graph graph) {
        if (!graph.isDirected()) {
            throw new IllegalArgumentException(
                    "Kahn's algorithm requires a directed graph"
            );
        }

        int n = graph.getVertexCount();
        int[] inDegree = new int[n];

        // Calculate the incoming edge count for every vertex.
        for (int vertex = 0; vertex < n; vertex++) {
            inDegree[vertex] = graph.getInDegree(vertex);
        }

        // Start with vertices that have no incoming edges.
        Queue<Integer> queue = new ArrayDeque<>();

        for (int vertex = 0; vertex < n; vertex++) {
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
                int next = edge.getTo();
                inDegree[next]--;

                if (inDegree[next] == 0) {
                    queue.offer(next);
                }
            }
        }

        // If not all vertices were processed, the graph contains a cycle.
        boolean acyclic = orderSize == n;
        int[] actualOrder = new int[orderSize];
        System.arraycopy(order, 0, actualOrder, 0, orderSize);

        return new KahnResult(actualOrder, acyclic);
    }
}