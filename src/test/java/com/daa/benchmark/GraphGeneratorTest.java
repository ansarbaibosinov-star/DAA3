
package com.daa.benchmark;

import com.daa.graph.Edge;
import com.daa.graph.Graph;
import org.junit.jupiter.api.Test;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.*;

class GraphGeneratorTest {

    @Test
    void generatesUndirectedGraphWithRequestedEdgeCount() {
        Graph graph = GraphGenerator.generate(
                100, 400, GraphGenerator.Type.UNDIRECTED, 42L
        );

        assertFalse(graph.isDirected());
        assertEquals(100, graph.getVertexCount());
        assertEquals(400, graph.getEdgeCount());
    }

    @Test
    void generatedUndirectedGraphIsConnected() {
        Graph graph = GraphGenerator.generate(
                100, 400, GraphGenerator.Type.UNDIRECTED, 42L
        );

        assertEquals(100, countReachableVertices(graph, 0));
    }

    @Test
    void generatedDagHasNoCycle() {
        Graph graph = GraphGenerator.generate(
                100, 400, GraphGenerator.Type.DAG, 42L
        );

        assertTrue(graph.isDirected());

        // Every generated DAG edge must go from a lower ID to a higher ID.
        for (Edge edge : graph.getEdges()) {
            assertTrue(
                    edge.getFrom() < edge.getTo(),
                    "DAG edge must point from a lower ID to a higher ID"
            );
        }
    }

    @Test
    void sameSeedProducesSameEdgesAndWeights() {
        Graph first = GraphGenerator.generate(
                20, 80, GraphGenerator.Type.DIRECTED, 42L
        );

        Graph second = GraphGenerator.generate(
                20, 80, GraphGenerator.Type.DIRECTED, 42L
        );

        assertEquals(first.getEdgeCount(), second.getEdgeCount());

        for (int i = 0; i < first.getEdgeCount(); i++) {
            Edge a = first.getEdges().get(i);
            Edge b = second.getEdges().get(i);

            assertEquals(a.getFrom(), b.getFrom());
            assertEquals(a.getTo(), b.getTo());
            assertEquals(a.getWeight(), b.getWeight());
        }
    }

    @Test
    void rejectsImpossibleDagEdgeCount() {
        assertThrows(
                IllegalArgumentException.class,
                () -> GraphGenerator.generate(
                        3, 4, GraphGenerator.Type.DAG, 42L
                )
        );
    }

    @Test
    void rejectsNegativeVertexOrEdgeCount() {
        assertThrows(
                IllegalArgumentException.class,
                () -> GraphGenerator.generate(
                        -1, 0, GraphGenerator.Type.DIRECTED, 42L
                )
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> GraphGenerator.generate(
                        5, -1, GraphGenerator.Type.DIRECTED, 42L
                )
        );
    }

    @Test
    void rejectsEdgesForEmptyGraph() {
        assertThrows(
                IllegalArgumentException.class,
                () -> GraphGenerator.generate(
                        0, 1, GraphGenerator.Type.DIRECTED, 42L
                )
        );
    }

    @Test
    void rejectsEdgesForSingleVertexUndirectedGraph() {
        assertThrows(
                IllegalArgumentException.class,
                () -> GraphGenerator.generate(
                        1, 1, GraphGenerator.Type.UNDIRECTED, 42L
                )
        );
    }

    @Test
    void generatedWeightsAreWithinExpectedRange() {
        Graph graph = GraphGenerator.generate(
                30, 100, GraphGenerator.Type.DIRECTED, 42L
        );

        for (Edge edge : graph.getEdges()) {
            assertTrue(edge.getWeight() >= -100);
            assertTrue(edge.getWeight() <= 100);
        }
    }

    @Test
    void generatedGraphContainsNoSelfLoops() {
        Graph graph = GraphGenerator.generate(
                30, 100, GraphGenerator.Type.DIRECTED, 42L
        );

        for (Edge edge : graph.getEdges()) {
            assertNotEquals(edge.getFrom(), edge.getTo());
        }
    }

    private static int countReachableVertices(Graph graph, int start) {
        boolean[] visited = new boolean[graph.getVertexCount()];
        Queue<Integer> queue = new ArrayDeque<>();

        visited[start] = true;
        queue.offer(start);

        int count = 0;

        while (!queue.isEmpty()) {
            int vertex = queue.poll();
            count++;

            for (Edge edge : graph.getAdjacentEdges(vertex)) {
                int next = edge.getTo();

                if (!visited[next]) {
                    visited[next] = true;
                    queue.offer(next);
                }
            }
        }

        return count;
    }
}
