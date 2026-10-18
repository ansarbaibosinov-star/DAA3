package com.daa.algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class DAGShortestPathResult {
    private final long[] distances;
    private final int[] parents;

    public DAGShortestPathResult(long[] distances, int[] parents) {
        this.distances = distances.clone();
        this.parents = parents.clone();
    }

    public long getDistanceTo(int vertex) {
        checkVertex(vertex);
        return distances[vertex];
    }

    public int getParentOf(int vertex) {
        checkVertex(vertex);
        return parents[vertex];
    }

    public long[] getDistances() {
        return distances.clone();
    }

    public List<Integer> getPathTo(int target) {
        checkVertex(target);

        if (distances[target] == Long.MAX_VALUE) {
            return Collections.emptyList();
        }

        List<Integer> path = new ArrayList<>();
        int current = target;

        while (current != -1) {
            path.add(current);
            current = parents[current];
        }

        Collections.reverse(path);
        return path;
    }

    private void checkVertex(int vertex) {
        if (vertex < 0 || vertex >= distances.length) {
            throw new IndexOutOfBoundsException(
                    "Invalid vertex: " + vertex
            );
        }
    }
}