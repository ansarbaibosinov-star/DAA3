
package com.daa.algorithms;

import com.daa.graph.Graph;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MSTTest {

    @Test
    void kruskalAndPrimFindSameMinimumWeight() {
        Graph graph = new Graph(5, false);

        graph.addEdge(0, 1, 4);
        graph.addEdge(0, 2, 3);
        graph.addEdge(1, 2, 1);
        graph.addEdge(1, 3, 2);
        graph.addEdge(2, 3, 4);
        graph.addEdge(3, 4, 2);
        graph.addEdge(2, 4, 5);

        MSTResult kruskal = Kruskal.findMST(graph);
        MSTResult prim = Prim.findMST(graph);

        assertEquals(8, kruskal.getTotalWeight());
        assertEquals(
                kruskal.getTotalWeight(),
                prim.getTotalWeight()
        );

        assertEquals(1, kruskal.getComponents());
        assertEquals(1, prim.getComponents());
    }

    @Test
    void handlesDisconnectedGraph() {
        Graph graph = new Graph(6, false);

        graph.addEdge(0, 1, 3);
        graph.addEdge(1, 2, 1);
        graph.addEdge(3, 4, 2);

        MSTResult kruskal = Kruskal.findMST(graph);
        MSTResult prim = Prim.findMST(graph);

        assertEquals(6, kruskal.getTotalWeight());
        assertEquals(6, prim.getTotalWeight());

        // Components: {0,1,2}, {3,4}, {5}
        assertEquals(3, kruskal.getComponents());
        assertEquals(3, prim.getComponents());
    }

    @Test
    void handlesNegativeEdgeWeights() {
        Graph graph = new Graph(4, false);

        graph.addEdge(0, 1, -5);
        graph.addEdge(1, 2, 2);
        graph.addEdge(0, 2, 4);
        graph.addEdge(2, 3, -1);
        graph.addEdge(1, 3, 3);

        MSTResult kruskal = Kruskal.findMST(graph);
        MSTResult prim = Prim.findMST(graph);

        assertEquals(-4, kruskal.getTotalWeight());
        assertEquals(-4, prim.getTotalWeight());
    }

    @Test
    void handlesSingleVertex() {
        Graph graph = new Graph(1, false);

        MSTResult kruskal = Kruskal.findMST(graph);
        MSTResult prim = Prim.findMST(graph);

        assertEquals(0, kruskal.getTotalWeight());
        assertEquals(0, prim.getTotalWeight());
        assertEquals(1, kruskal.getComponents());
        assertEquals(1, prim.getComponents());
    }

    @Test
    void handlesEmptyGraph() {
        Graph graph = new Graph(0, false);

        MSTResult kruskal = Kruskal.findMST(graph);
        MSTResult prim = Prim.findMST(graph);

        assertEquals(0, kruskal.getTotalWeight());
        assertEquals(0, prim.getTotalWeight());
        assertEquals(0, kruskal.getComponents());
        assertEquals(0, prim.getComponents());
    }

    @Test
    void rejectsDirectedGraph() {
        Graph graph = new Graph(3, true);
        graph.addEdge(0, 1, 1);

        assertThrows(
                IllegalArgumentException.class,
                () -> Kruskal.findMST(graph)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> Prim.findMST(graph)
        );
    }

    @Test
    void updatesMetrics() {
        Graph graph = new Graph(4, false);

        graph.addEdge(0, 1, 4);
        graph.addEdge(1, 2, 2);
        graph.addEdge(2, 3, 1);
        graph.addEdge(0, 3, 8);

        Metrics kruskalMetrics = new Metrics();
        Metrics primMetrics = new Metrics();

        MSTResult kruskal =
                Kruskal.findMST(graph, kruskalMetrics);

        MSTResult prim =
                Prim.findMST(graph, primMetrics);

        assertEquals(
                kruskal.getTotalWeight(),
                prim.getTotalWeight()
        );

        assertTrue(kruskalMetrics.getComparisons() > 0
                || kruskalMetrics.getEdgeScans() > 0);

        assertTrue(primMetrics.getComparisons() > 0
                || primMetrics.getEdgeScans() > 0);
    }
}
