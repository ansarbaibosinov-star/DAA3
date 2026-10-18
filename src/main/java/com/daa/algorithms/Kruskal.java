package com.daa.algorithms;

import com.daa.graph.Edge;
import com.daa.graph.Graph;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class Kruskal {

    private Kruskal() {
    }

    public static MSTResult findMST(Graph graph) {
        return findMST(graph, null);
    }

    public static MSTResult findMST(Graph graph, Metrics metrics) {
        if (graph.isDirected()) {
            throw new IllegalArgumentException(
                    "Kruskal requires an undirected graph"
            );
        }

        List<Edge> sortedEdges = new ArrayList<>(graph.getEdges());

        sortedEdges.sort((a, b) -> {
            if (metrics != null) {
                metrics.recordComparison();
            }
            return Integer.compare(a.getWeight(), b.getWeight());
        });

        DSU dsu = new DSU(graph.getVertexCount(), metrics);
        List<Edge> selectedEdges = new ArrayList<>();
        long totalWeight = 0;

        for (Edge edge : sortedEdges) {
            if (metrics != null) {
                metrics.recordEdgeScan();
            }

            int rootFrom = dsu.find(edge.getFrom());
            int rootTo = dsu.find(edge.getTo());

            if (rootFrom != rootTo) {
                dsu.union(rootFrom, rootTo);
                selectedEdges.add(edge);
                totalWeight += edge.getWeight();
            }
        }

        return new MSTResult(
                selectedEdges,
                totalWeight,
                dsu.components()
        );
    }
}