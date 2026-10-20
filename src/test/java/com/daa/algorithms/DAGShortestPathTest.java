
package com.daa.algorithms;

import com.daa.graph.Graph;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DAGShortestPathTest {

    @Test
    void shortestPathsShouldCalculateCorrectDistances() {
        Graph graph = new Graph(5, true);

        graph.addEdge(0, 1, 3);
        graph.addEdge(0, 2, 6);
        graph.addEdge(1, 2, 2);
        graph.addEdge(1, 3, 4);
        graph.addEdge(2, 3, 1);
        graph.addEdge(3, 4, 2);

        DAGShortestPathResult result =
                DAGShortestPath.findShortestPaths(graph, 0);

        long[] distances = result.getDistances();

        assertArrayEquals(
                new long[]{0, 3, 5, 6, 8},
                distances
        );
    }

    @Test
    void shortestPathsShouldSupportNegativeWeights() {
        Graph graph = new Graph(4, true);

        graph.addEdge(0, 1, 4);
        graph.addEdge(0, 2, 5);
        graph.addEdge(1, 2, -3);
        graph.addEdge(2, 3, 2);

        DAGShortestPathResult result =
                DAGShortestPath.findShortestPaths(graph, 0);

        long[] distances = result.getDistances();

        assertArrayEquals(
                new long[]{0, 4, 1, 3},
                distances
        );
    }

    @Test
    void unreachableVertexHasInfiniteDistance() {
        Graph graph = new Graph(4, true);

        graph.addEdge(0, 1, 5);
        graph.addEdge(1, 2, 3);

        DAGShortestPathResult result =
                DAGShortestPath.findShortestPaths(graph, 0);

        long[] distances = result.getDistances();

        assertEquals(0L, distances[0]);
        assertEquals(5L, distances[1]);
        assertEquals(8L, distances[2]);
        assertEquals(Long.MAX_VALUE, distances[3]);
    }

    @Test
    void shortestPathsShouldRejectCycles() {
        Graph graph = new Graph(3, true);

        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 1);
        graph.addEdge(2, 0, 1);

        assertThrows(
                IllegalArgumentException.class,
                () -> DAGShortestPath.findShortestPaths(graph, 0)
        );
    }

    @Test
    void shortestPathsShouldRejectUndirectedGraph() {
        Graph graph = new Graph(3, false);

        graph.addEdge(0, 1, 2);
        graph.addEdge(1, 2, 3);

        assertThrows(
                IllegalArgumentException.class,
                () -> DAGShortestPath.findShortestPaths(graph, 0)
        );
    }

    @Test
    void shortestPathsShouldHandleSingleVertex() {
        Graph graph = new Graph(1, true);

        DAGShortestPathResult result =
                DAGShortestPath.findShortestPaths(graph, 0);

        assertArrayEquals(
                new long[]{0},
                result.getDistances()
        );
    }

    @Test
    void shortestPathsShouldRejectInvalidSource() {
        Graph graph = new Graph(3, true);

        assertThrows(
                IllegalArgumentException.class,
                () -> DAGShortestPath.findShortestPaths(graph, -1)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> DAGShortestPath.findShortestPaths(graph, 3)
        );
    }

    @Test
    void shortestPathsShouldHandleDisconnectedDag() {
        Graph graph = new Graph(5, true);

        graph.addEdge(0, 1, 2);
        graph.addEdge(2, 3, 4);

        DAGShortestPathResult result =
                DAGShortestPath.findShortestPaths(graph, 0);

        long[] distances = result.getDistances();

        assertEquals(0L, distances[0]);
        assertEquals(2L, distances[1]);
        assertEquals(Long.MAX_VALUE, distances[2]);
        assertEquals(Long.MAX_VALUE, distances[3]);
        assertEquals(Long.MAX_VALUE, distances[4]);
    }
}
