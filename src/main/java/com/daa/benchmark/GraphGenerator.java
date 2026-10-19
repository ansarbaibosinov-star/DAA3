package com.daa.benchmark;

import com.daa.graph.Graph;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public final class GraphGenerator {

    public enum Type {
        UNDIRECTED,
        DIRECTED,
        DAG
    }

    private GraphGenerator() {
    }

    public static Graph generate(int n, int m, Type type, long seed) {
        if (n < 0) {
            throw new IllegalArgumentException(
                    "Number of vertices cannot be negative"
            );
        }

        if (m < 0) {
            throw new IllegalArgumentException(
                    "Number of edges cannot be negative"
            );
        }

        if (type == null) {
            throw new IllegalArgumentException(
                    "Graph type cannot be null"
            );
        }

        long maxEdges;

        if (type == Type.DIRECTED) {
            maxEdges = (long) n * (n - 1);
        } else {
            maxEdges = (long) n * (n - 1) / 2;
        }

        if (m > maxEdges) {
            throw new IllegalArgumentException(
                    "Too many edges for this graph type"
            );
        }

        if (type == Type.UNDIRECTED && n > 0 && m < n - 1) {
            throw new IllegalArgumentException(
                    "A connected undirected graph needs at least n - 1 edges"
            );
        }

        Graph graph = new Graph(n, type != Type.UNDIRECTED);
        Random random = new Random(seed);
        Set<Long> usedEdges = new HashSet<>();

        // For an undirected graph, first create a spanning tree.
        // This guarantees that the graph is connected.
        if (type == Type.UNDIRECTED) {
            for (int vertex = 1; vertex < n; vertex++) {
                int parent = random.nextInt(vertex);
                int weight = random.nextInt(201) - 100;

                graph.addEdge(parent, vertex, weight);
                usedEdges.add(edgeKey(parent, vertex, n, type));
            }
        }

        // Add remaining edges without duplicates or self-loops.
        while (graph.getEdgeCount() < m) {
            int from = random.nextInt(n);
            int to = random.nextInt(n);

            if (from == to) {
                continue;
            }

            if (type == Type.DAG && from > to) {
                int temp = from;
                from = to;
                to = temp;
            }

            long key = edgeKey(from, to, n, type);

            if (!usedEdges.add(key)) {
                continue;
            }

            int weight = random.nextInt(201) - 100;
            graph.addEdge(from, to, weight);
        }

        return graph;
    }

    private static long edgeKey(
            int from,
            int to,
            int n,
            Type type
    ) {
        if (type != Type.DIRECTED && from > to) {
            int temp = from;
            from = to;
            to = temp;
        }

        return (long) from * n + to;
    }
}
