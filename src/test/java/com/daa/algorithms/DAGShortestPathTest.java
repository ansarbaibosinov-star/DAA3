
package com.daa.algorithms;

import com.daa.graph.Graph;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DAGShortestPathTest {

    @Test
    void findsShortestPathsInDag() {
        Graph graph = new Graph(5, true);

        graph.addEdge(0, 1, 3);
        graph.addEdge(0, 2, 6);
        graph.addEdge(1, 2, 2);
        graph.addEdge(1, 3, 4);
        graph.addEdge(2, 3, 1);
        graph.addEdge(3, 4, 2);

        DAGShortestPathResult result =
                DAGShortestPath.findShortestPaths(graph);

        long[] distances = result.getDistances();

        assertEquals(0, distances[0]);
        assertEquals(3, distances[1]);
        assertEquals(5, distances[2]);
        assertEquals(6, distances[3]);
        assertEquals(8, distances[4]);
    }

    @Test
    void supportsNegativeEdgeWeights() {
        Graph graph = new Graph(4, true);

        graph.addEdge(0, 1, 5);
        graph.addEdge(0, 2, 2);
        graph.addEdge(2, 1, -4);
        graph.addEdge(1, 3, 3);
        graph.addEdge(2, 3, 10);

        DAGShortestPathResult result =
                DAGShortestPath.findShortestPaths(graph);

        long[] distances = result.getDistances();

        assertEquals(0, distances[0]);
        assertEquals(-2, distances[1]);
        assertEquals(2, distances[2]);
        assertEquals(1, distances[3]);
    }

    @Test
    void unreachableVertexHasInfiniteDistance() {
        Graph graph = new Graph(3, true);
        graph.addEdge(0, 1, 7);

        DAGShortestPathResult result =
                DAGShortestPath.findShortestPaths(graph);

        assertEquals(0, result.getDistances()[0]);
        assertEquals(7, result.getDistances()[1]);
        assertEquals(Long.MAX_VALUE, result.getDistances()[2]);
        assertTrue(result.getPathTo(2).isEmpty());
    }

    @Test
    void restoresShortestPath() {
        Graph graph = new Graph(4, true);

        graph.addEdge(0, 1, 2);
        graph.addEdge(0, 2, 10);
        graph.addEdge(1, 2, 3);
        graph.addEdge(2, 3, 1);

        DAGShortestPathResult result =
                DAGShortestPath.findShortestPaths(graph);

        assertEquals(List.of(0, 1, 2, 3), result.getPathTo(3));
    }

    @Test
    void everySourceStartsAtZero() {
        Graph graph = new Graph(4, true);

        graph.addEdge(0, 2, 5);
        graph.addEdge(1, 2, 2);

        DAGShortestPathResult result =
                DAGShortestPath.findShortestPaths(graph);

        assertEquals(0, result.getDistances()[0]);
        assertEquals(0, result.getDistances()[1]);
        assertEquals(0, result.getDistances()[3]);
        assertEquals(2, result.getDistances()[2]);
    }

    @Test
    void rejectsCyclicGraph() {
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
        graph.addEdge(0, 1, 5);

        assertThrows(
                IllegalArgumentException.class,
                () -> DAGShortestPath.findShortestPaths(graph)
        );
    }

    @Test
    void updatesMetrics() {
        Graph graph = new Graph(3, true);

        graph.addEdge(0, 1, 4);
        graph.addEdge(1, 2, 3);

        Metrics metrics = new Metrics();

        DAGShortestPathResult result =
                DAGShortestPath.findShortestPaths(graph, metrics);

        assertEquals(7, result.getDistances()[2]);
        assertTrue(metrics.getEdgeScans() > 0);
        assertTrue(metrics.getComparisons() > 0);
    }
}
