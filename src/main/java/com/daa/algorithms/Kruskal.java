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
        if (graph.isDirected()) {
            throw new IllegalArgumentException(
                    "Kruskal requires an undirected graph"
            );
        }

        List<Edge> sortedEdges = new ArrayList<>(graph.getEdges());

        sortedEdges.sort(
                Comparator.comparingInt(Edge::getWeight)
        );

        DSU dsu = new DSU(graph.getVertexCount());
        List<Edge> selectedEdges = new ArrayList<>();
        long totalWeight = 0;

        for (Edge edge : sortedEdges) {
            if (dsu.find(edge.getFrom())
                    != dsu.find(edge.getTo())) {

                dsu.union(edge.getFrom(), edge.getTo());

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
