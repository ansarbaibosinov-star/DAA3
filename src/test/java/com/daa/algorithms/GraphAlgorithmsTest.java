package com.daa.algorithms;

import com.daa.graph.Edge;
import com.daa.graph.Graph;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class GraphAlgorithmsTest {

    @Test
    void kosarajuFindsThreeStronglyConnectedComponents() {
        Graph graph = new Graph(6, true);

        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 1);
        graph.addEdge(2, 0, 1);

        graph.addEdge(2, 3, 1);
        graph.addEdge(3, 4, 1);
        graph.addEdge(4, 3, 1);
        graph.addEdge(4, 5, 1);

        SCCResult result = Kosaraju.findSCC(graph);

        assertEquals(3, result.getComponentCount());
        assertEquals(result.getComponentOf(0), result.getComponentOf(2));
        assertEquals(result.getComponentOf(3), result.getComponentOf(4));
        assertNotEquals(result.getComponentOf(0), result.getComponentOf(3));
        assertNotEquals(result.getComponentOf(4), result.getComponentOf(5));
    }

    @Test
    void kosarajuHandlesDisconnectedVertices() {
        Graph graph = new Graph(4, true);
        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 0, 1);

        SCCResult result = Kosaraju.findSCC(graph);

        assertEquals(3, result.getComponentCount());
    }

    @Test
    void kosarajuHandlesEmptyGraph() {
        Graph graph = new Graph(0, true);

        SCCResult result = Kosaraju.findSCC(graph);

        assertEquals(0, result.getComponentCount());
    }

    @Test
    void kosarajuRejectsUndirectedGraph() {
        Graph graph = new Graph(2, false);

        assertThrows(
                IllegalArgumentException.class,
                () -> Kosaraju.findSCC(graph)
        );
    }

    @Test
    void kahnProducesValidTopologicalOrder() {
        Graph graph = new Graph(6, true);
        graph.addEdge(5, 2, 1);
        graph.addEdge(5, 0, 1);
        graph.addEdge(4, 0, 1);
        graph.addEdge(4, 1, 1);
        graph.addEdge(2, 3, 1);
        graph.addEdge(3, 1, 1);

        KahnResult result = Kahn.topologicalSort(graph);

        assertTrue(result.isAcyclic());
        assertEquals(6, result.getOrder().length);
        assertValidTopologicalOrder(graph, result.getOrder());
    }

    @Test
    void kahnDetectsCycle() {
        Graph graph = new Graph(3, true);
        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 1);
        graph.addEdge(2, 0, 1);

        KahnResult result = Kahn.topologicalSort(graph);

        assertFalse(result.isAcyclic());
        assertTrue(result.getOrder().length < graph.getVertexCount());
    }

    @Test
    void kahnHandlesDisconnectedDag() {
        Graph graph = new Graph(5, true);
        graph.addEdge(0, 1, 1);
        graph.addEdge(2, 3, 1);

        KahnResult result = Kahn.topologicalSort(graph);

        assertTrue(result.isAcyclic());
        assertEquals(5, result.getOrder().length);
        assertValidTopologicalOrder(graph, result.getOrder());
    }

    @Test
    void kahnHandlesEmptyGraph() {
        Graph graph = new Graph(0, true);

        KahnResult result = Kahn.topologicalSort(graph);

        assertTrue(result.isAcyclic());
        assertEquals(0, result.getOrder().length);
    }

    @Test
    void kahnRejectsUndirectedGraph() {
        Graph graph = new Graph(2, false);

        assertThrows(
                IllegalArgumentException.class,
                () -> Kahn.topologicalSort(graph)
        );
    }

    @Test
    void kosarajuHandlesLongPathWithoutRecursion() {
        int n = 100_000;
        Graph graph = new Graph(n, true);

        for (int i = 0; i < n - 1; i++) {
            graph.addEdge(i, i + 1, 1);
        }

        SCCResult result = Kosaraju.findSCC(graph);

        assertEquals(n, result.getComponentCount());
    }

    private static void assertValidTopologicalOrder(
            Graph graph,
            int[] order
    ) {
        int n = graph.getVertexCount();
        assertEquals(n, order.length);

        int[] position = new int[n];
        Set<Integer> seen = new HashSet<>();

        for (int i = 0; i < order.length; i++) {
            assertTrue(seen.add(order[i]), "Duplicate vertex");
            position[order[i]] = i;
        }

        for (Edge edge : graph.getEdges()) {
            assertTrue(
                    position[edge.getFrom()] < position[edge.getTo()],
                    "An edge violates the topological order"
            );
        }
    }
    @Test
    void kosarajuRecordsMetricsOnLongPath() {
        int n = 100_000;
        Graph graph = new Graph(n, true);

        for (int i = 0; i < n - 1; i++) {
            graph.addEdge(i, i + 1, 1);
        }

        Metrics metrics = new Metrics();
        SCCResult result = Kosaraju.findSCC(graph, metrics);

        assertEquals(n, result.getComponentCount());
        assertTrue(metrics.getEdgeScans() > 0);
        assertTrue(metrics.getMaxDepth() > 0);
    }

    @Test
    void kahnAndDagShortestPathRecordMetrics() {
        Graph graph = new Graph(4, true);
        graph.addEdge(0, 1, 2);
        graph.addEdge(0, 2, 5);
        graph.addEdge(1, 3, -4);
        graph.addEdge(2, 3, 1);

        Metrics kahnMetrics = new Metrics();
        KahnResult order = Kahn.topologicalSort(graph, kahnMetrics);

        assertTrue(order.isAcyclic());
        assertTrue(kahnMetrics.getEdgeScans() > 0);
        assertTrue(kahnMetrics.getComparisons() > 0);

        Metrics pathMetrics = new Metrics();
        DAGShortestPathResult paths =
                DAGShortestPath.findShortestPaths(graph, pathMetrics);

        assertEquals(-2, paths.getDistanceTo(3));
        assertTrue(pathMetrics.getEdgeScans() > 0);
        assertTrue(pathMetrics.getComparisons() > 0);
    }
}