package com.daa.algorithms;

import com.daa.graph.Edge;
import com.daa.graph.Graph;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public final class Prim {

    private Prim() {
    }

    public static MSTResult findMST(Graph graph) {
        return findMST(graph, null);
    }

    public static MSTResult findMST(Graph graph, Metrics metrics) {
        if (graph.isDirected()) {
            throw new IllegalArgumentException(
                    "Prim requires an undirected graph"
            );
        }

        int n = graph.getVertexCount();
        boolean[] inTree = new boolean[n];
        List<Edge> selectedEdges = new ArrayList<>();

        Comparator<Edge> comparator = (a, b) -> {
            if (metrics != null) {
                metrics.recordComparison();
            }
            return Integer.compare(a.getWeight(), b.getWeight());
        };

        PriorityQueue<Edge> queue = new PriorityQueue<>(comparator);

        long totalWeight = 0;
        int components = 0;

        for (int start = 0; start < n; start++) {
            if (inTree[start]) {
                continue;
            }

            components++;
            inTree[start] = true;
            addAdjacentEdges(graph, start, inTree, queue, metrics);

            while (!queue.isEmpty()) {
                Edge edge = queue.poll();

                boolean fromInside = inTree[edge.getFrom()];
                boolean toInside = inTree[edge.getTo()];

                if (fromInside && toInside) {
                    continue;
                }

                int next = fromInside ? edge.getTo() : edge.getFrom();

                if (inTree[next]) {
                    continue;
                }

                inTree[next] = true;
                selectedEdges.add(edge);
                totalWeight += edge.getWeight();

                addAdjacentEdges(
                        graph, next, inTree, queue, metrics
                );
            }
        }

        return new MSTResult(selectedEdges, totalWeight, components);
    }

    private static void addAdjacentEdges(
            Graph graph,
            int vertex,
            boolean[] inTree,
            PriorityQueue<Edge> queue,
            Metrics metrics
    ) {
        for (Edge edge : graph.getAdjacentEdges(vertex)) {
            if (metrics != null) {
                metrics.recordEdgeScan();
            }

            if (!inTree[edge.getTo()]) {
                queue.offer(edge);
            }
        }
    }
}