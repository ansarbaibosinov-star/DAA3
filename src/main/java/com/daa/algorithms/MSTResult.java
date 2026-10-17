package com.daa.algorithms;

import com.daa.graph.Edge;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class MSTResult {
    private final List<Edge> edges;
    private final long totalWeight;
    private final int components;

    public MSTResult(List<Edge> edges, long totalWeight, int components) {
        this.edges = Collections.unmodifiableList(
                new ArrayList<>(edges)
        );
        this.totalWeight = totalWeight;
        this.components = components;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public long getTotalWeight() {
        return totalWeight;
    }

    public int getComponents() {
        return components;
    }
}
