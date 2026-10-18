package com.daa.algorithms;

import com.daa.graph.Graph;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DAGShortestPathTest {

    @Test
    void findsShortestPathsWithNegativeWeights() {
        Graph graph = new Graph(4, true);
        graph.addEdge(0, 1, 2);
        graph.addEdge(0, 2, 5);
        graph.addEdge(1, 3, -4);
        graph.addEdge(2, 3, 1);

        DAGShortestPathResult result =
                DAGShortestPath.findShortestPaths(graph);

        assertEquals(0, result.getDistanceTo(0));
        assertEquals(2, result.getDistanceTo(1));
        assertEquals(5, result.getDistanceTo(2));
        assertEquals(-2, result.getDistanceTo(3));

        assertEquals(List.of(0, 1, 3), result.getPathTo(3));
    }

    @Test
    void everySourceStartsAtZero() {
        Graph graph = new Graph(5, true);
        graph.addEdge(0, 2, 4);
        graph.addEdge(1, 2, 2);
        graph.addEdge(3, 4, 7);

        DAGShortestPathResult result =
                DAGShortestPath.findShortestPaths(graph);

        assertEquals(0, result.getDistanceTo(0));
        assertEquals(0, result.getDistanceTo(1));
        assertEquals(0, result.getDistanceTo(3));

        // Vertex 2 can be reached from sources 0 and 1.
        assertEquals(2, result.getDistanceTo(2));
        assertEquals(List.of(1, 2), result.getPathTo(2));

        assertEquals(7, result.getDistanceTo(4));
        assertEquals(List.of(3, 4), result.getPathTo(4));
    }

    @Test
    void rejectsGraphWithCycle() {
        Graph graph = new Graph(3, true);
        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 1);
        graph.addEdge(2, 0, 1);

        assertThrows(
                IllegalArgumentException.class,
                () -> DAGShortestPath.findShortestPaths(graph)
        );
    }

    @Test
    void rejectsUndirectedGraph() {
        Graph graph = new Graph(2, false);
        graph.addEdge(0, 1, 3);

        assertThrows(
                IllegalArgumentException.class,
                () -> DAGShortestPath.findShortestPaths(graph)
        );
    }

    @Test
    void handlesSingleVertex() {
        Graph graph = new Graph(1, true);

        DAGShortestPathResult result =
                DAGShortestPath.findShortestPaths(graph);

        assertEquals(0, result.getDistanceTo(0));
        assertEquals(List.of(0), result.getPathTo(0));
    }

    @Test
    void handlesEmptyGraph() {
        Graph graph = new Graph(0, true);

        DAGShortestPathResult result =
                DAGShortestPath.findShortestPaths(graph);

        assertEquals(0, result.getDistances().length);
    }

    @Test
    void rejectsInvalidVertexForDistanceAndPath() {
        Graph graph = new Graph(2, true);

        DAGShortestPathResult result =
                DAGShortestPath.findShortestPaths(graph);

        assertThrows(
                IndexOutOfBoundsException.class,
                () -> result.getDistanceTo(2)
        );

        assertThrows(
                IndexOutOfBoundsException.class,
                () -> result.getPathTo(-1)
        );
    }
}