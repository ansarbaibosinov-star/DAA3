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
        if (graph.isDirected()) {
            throw new IllegalArgumentException(
                    "Prim requires an undirected graph"
            );
        }

        int n = graph.getVertexCount();

        boolean[] inTree = new boolean[n];
        List<Edge> selectedEdges = new ArrayList<>();

        PriorityQueue<Edge> queue = new PriorityQueue<>(
                Comparator.comparingInt(Edge::getWeight)
        );

        long totalWeight = 0;
        int components = 0;

        // Start at vertex 0, then process other components if needed.
        for (int start = 0; start < n; start++) {
            if (inTree[start]) {
                continue;
            }

            components++;
            inTree[start] = true;

            for (Edge edge : graph.getAdjacentEdges(start)) {
                queue.offer(edge);
            }

            while (!queue.isEmpty()) {
                Edge edge = queue.poll();

                int from = edge.getFrom();
                int to = edge.getTo();

                // Ignore edges whose endpoints are both already in the tree.
                if (inTree[from] && inTree[to]) {
                    continue;
                }

                // An edge leaving the current tree must have one new endpoint.
                int next = inTree[from] ? to : from;

                if (inTree[next]) {
                    continue;
                }

                inTree[next] = true;
                selectedEdges.add(edge);
                totalWeight += edge.getWeight();

                for (Edge adjacent : graph.getAdjacentEdges(next)) {
                    if (!inTree[adjacent.getTo()]) {
                        queue.offer(adjacent);
                    }
                }
            }
        }

        return new MSTResult(
                selectedEdges,
                totalWeight,
                components
        );
    }
}
