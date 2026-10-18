package com.daa.benchmark;

import com.daa.graph.Graph;

import java.util.Random;

public final class GraphGenerator {

    public enum Type {
        UNDIRECTED,
        DIRECTED,
        DAG
    }

    private GraphGenerator() {
    }

    public static Graph generate(int n, int m, Type type, long seed) {
        if (n < 0 || m < 0) {
            throw new IllegalArgumentException(
                    "n and m must be non-negative"
            );
        }

        if (n == 0 && m > 0) {
            throw new IllegalArgumentException(
                    "An empty graph cannot have edges"
            );
        }

        if (type == Type.DAG && (long) m > (long) n * (n - 1) / 2) {
            throw new IllegalArgumentException(
                    "Too many edges for a simple DAG"
            );
        }

        if (type == Type.UNDIRECTED && n > 0 && m < n - 1) {
            throw new IllegalArgumentException(
                    "A connected undirected graph needs at least n - 1 edges"
            );
        }

        Graph graph = new Graph(
                n,
                type != Type.UNDIRECTED
        );

        Random random = new Random(seed);

        if (type == Type.UNDIRECTED && n > 1) {
            // First create a connected spanning tree.
            for (int vertex = 1; vertex < n; vertex++) {
                int parent = random.nextInt(vertex);
                int weight = random.nextInt(201) - 100;
                graph.addEdge(parent, vertex, weight);
            }
        }

        while (graph.getEdgeCount() < m) {
            int from = random.nextInt(n);
            int to = random.nextInt(n);

            if (from == to) {
                continue;
            }

            if (type == Type.DAG) {
                // All edges point from a smaller ID to a larger ID.
                if (from > to) {
                    int temp = from;
                    from = to;
                    to = temp;
                }
            }

            int weight = random.nextInt(201) - 100;
            graph.addEdge(from, to, weight);
        }

        return graph;
    }
}