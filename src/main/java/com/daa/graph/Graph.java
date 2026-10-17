package com.daa.graph;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Graph {
    private final int vertexCount;
    private final boolean directed;

    private final List<List<Edge>> adjacencyList;
    private final List<Edge> edges;

    public Graph(int vertexCount, boolean directed) {
        if (vertexCount < 0) {
            throw new IllegalArgumentException(
                    "Vertex count cannot be negative"
            );
        }

        this.vertexCount = vertexCount;
        this.directed = directed;

        this.adjacencyList = new ArrayList<>(vertexCount);
        this.edges = new ArrayList<>();

        for (int i = 0; i < vertexCount; i++) {
            adjacencyList.add(new ArrayList<>());
        }
    }

    public void addEdge(int from, int to, int weight) {
        checkVertex(from);
        checkVertex(to);

        Edge edge = new Edge(from, to, weight);

        // Store each original edge once.
        edges.add(edge);

        // Add the edge to the adjacency list.
        adjacencyList.get(from).add(edge);

        // For an undirected graph, add the reverse arc too.
        if (!directed) {
            Edge reverseEdge = new Edge(to, from, weight);
            adjacencyList.get(to).add(reverseEdge);
        }
    }

    private void checkVertex(int vertex) {
        if (vertex < 0 || vertex >= vertexCount) {
            throw new IndexOutOfBoundsException(
                    "Invalid vertex: " + vertex
            );
        }
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public int getEdgeCount() {
        return edges.size();
    }

    public boolean isDirected() {
        return directed;
    }

    public List<Edge> getEdges() {
        return Collections.unmodifiableList(edges);
    }

    public List<Edge> getAdjacentEdges(int vertex) {
        checkVertex(vertex);
        return Collections.unmodifiableList(
                adjacencyList.get(vertex)
        );
    }

    public int getOutDegree(int vertex) {
        checkVertex(vertex);
        return adjacencyList.get(vertex).size();
    }

    public int getInDegree(int vertex) {
        checkVertex(vertex);

        int degree = 0;

        for (Edge edge : edges) {
            if (edge.getTo() == vertex) {
                degree++;
            }

            if (!directed && edge.getFrom() == vertex) {
                degree++;
            }
        }


        return degree;
    }

    public Graph reverse() {
        Graph reversed = new Graph(vertexCount, directed);

        for (Edge edge : edges) {
            reversed.addEdge(
                    edge.getTo(),
                    edge.getFrom(),
                    edge.getWeight()
            );
        }

        return reversed;
    }
}
