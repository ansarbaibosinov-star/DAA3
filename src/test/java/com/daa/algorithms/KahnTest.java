
package com.daa.algorithms;

import com.daa.graph.Edge;
import com.daa.graph.Graph;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KahnTest {

    @Test
    void producesValidTopologicalOrder() {
        Graph graph = new Graph(6, true);

        graph.addEdge(5, 2, 1);
        graph.addEdge(5, 0, 1);
        graph.addEdge(4, 0, 1);
        graph.addEdge(4, 1, 1);
        graph.addEdge(2, 3, 1);
        graph.addEdge(3, 1, 1);

        KahnResult result = Kahn.topologicalSort(graph);

        assertTrue(result.isAcyclic());

        int[] order = result.getOrder();
        assertEquals(6, order.length);

        int[] position = new int[6];

        for (int i = 0; i < order.length; i++) {
            position[order[i]] = i;
        }

        for (Edge edge : graph.getEdges()) {
            assertTrue(
                    position[edge.getFrom()] < position[edge.getTo()],
                    "Every edge must go forward in the topological order"
            );
        }
    }

    @Test
    void detectsCycle() {
        Graph graph = new Graph(3, true);

        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 1);
        graph.addEdge(2, 0, 1);

        KahnResult result = Kahn.topologicalSort(graph);

        assertFalse(result.isAcyclic());
        assertTrue(result.getOrder().length < 3);
    }

    @Test
    void handlesDisconnectedDag() {
        Graph graph = new Graph(5, true);

        graph.addEdge(0, 1, 1);
        graph.addEdge(2, 3, 1);

        KahnResult result = Kahn.topologicalSort(graph);

        assertTrue(result.isAcyclic());
        assertEquals(5, result.getOrder().length);
    }

    @Test
    void handlesEmptyGraph() {
        Graph graph = new Graph(0, true);

        KahnResult result = Kahn.topologicalSort(graph);

        assertTrue(result.isAcyclic());
        assertEquals(0, result.getOrder().length);
    }

    @Test
    void rejectsUndirectedGraph() {
        Graph graph = new Graph(2, false);
        graph.addEdge(0, 1, 1);

        assertThrows(
                IllegalArgumentException.class,
                () -> Kahn.topologicalSort(graph)
        );
    }

    @Test
    void updatesMetrics() {
        Graph graph = new Graph(3, true);

        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 1);

        Metrics metrics = new Metrics();

        KahnResult result = Kahn.topologicalSort(graph, metrics);

        assertTrue(result.isAcyclic());
        assertTrue(metrics.getEdgeScans() > 0);
        assertTrue(metrics.getComparisons() > 0);
    }
}
