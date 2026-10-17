package com.daa.algorithms;

import com.daa.graph.Graph;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class MSTTest {

    @Test
    void kruskalAndPrimShouldReturnSameWeight() {
        Graph graph = new Graph(4, false);
        graph.addEdge(0, 1, 10);
        graph.addEdge(0, 2, 6);
        graph.addEdge(0, 3, 5);
        graph.addEdge(1, 3, 15);
        graph.addEdge(2, 3, 4);

        MSTResult kruskal = Kruskal.findMST(graph);
        MSTResult prim = Prim.findMST(graph);

        assertEquals(19, kruskal.getTotalWeight());
        assertEquals(
                kruskal.getTotalWeight(),
                prim.getTotalWeight()
        );

        assertEquals(3, kruskal.getEdges().size());
        assertEquals(3, prim.getEdges().size());
        assertEquals(1, kruskal.getComponents());
        assertEquals(1, prim.getComponents());
    }

    @Test
    void shouldReturnMinimumSpanningForestForDisconnectedGraph() {
        Graph graph = new Graph(5, false);

        graph.addEdge(0, 1, 4);
        graph.addEdge(1, 2, 2);
        graph.addEdge(3, 4, 7);

        MSTResult kruskal = Kruskal.findMST(graph);
        MSTResult prim = Prim.findMST(graph);

        assertEquals(13, kruskal.getTotalWeight());
        assertEquals(13, prim.getTotalWeight());

        assertEquals(3, kruskal.getEdges().size());
        assertEquals(3, prim.getEdges().size());

        assertEquals(2, kruskal.getComponents());
        assertEquals(2, prim.getComponents());
    }

    @Test
    void shouldHandleIsolatedVertices() {
        Graph graph = new Graph(3, false);

        MSTResult kruskal = Kruskal.findMST(graph);
        MSTResult prim = Prim.findMST(graph);

        assertEquals(0, kruskal.getTotalWeight());
        assertEquals(0, prim.getTotalWeight());

        assertEquals(3, kruskal.getComponents());
        assertEquals(3, prim.getComponents());

        assertTrue(kruskal.getEdges().isEmpty());
        assertTrue(prim.getEdges().isEmpty());
    }

    @Test
    void shouldIgnoreSelfLoopsAndChooseCheaperParallelEdge() {
        Graph graph = new Graph(2, false);

        graph.addEdge(0, 0, -100);
        graph.addEdge(0, 1, 10);
        graph.addEdge(0, 1, 3);

        MSTResult kruskal = Kruskal.findMST(graph);
        MSTResult prim = Prim.findMST(graph);

        assertEquals(3, kruskal.getTotalWeight());
        assertEquals(3, prim.getTotalWeight());
        assertEquals(1, kruskal.getEdges().size());
        assertEquals(1, prim.getEdges().size());
    }

    @Test
    void shouldHandleNegativeWeights() {
        Graph graph = new Graph(3, false);

        graph.addEdge(0, 1, -5);
        graph.addEdge(1, 2, 2);
        graph.addEdge(0, 2, 10);

        MSTResult kruskal = Kruskal.findMST(graph);
        MSTResult prim = Prim.findMST(graph);

        assertEquals(-3, kruskal.getTotalWeight());
        assertEquals(-3, prim.getTotalWeight());
    }

    @Test
    void shouldRejectDirectedGraphs() {
        Graph graph = new Graph(2, true);
        graph.addEdge(0, 1, 5);

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
    void shouldHandleEmptyGraph() {
        Graph graph = new Graph(0, false);

        MSTResult kruskal = Kruskal.findMST(graph);
        MSTResult prim = Prim.findMST(graph);

        assertEquals(0, kruskal.getTotalWeight());
        assertEquals(0, prim.getTotalWeight());
        assertEquals(0, kruskal.getComponents());
        assertEquals(0, prim.getComponents());
    }

    @Test
    void shouldAgreeOnOneHundredRandomConnectedGraphs() {
        Random random = new Random(42);

        for (int test = 0; test < 100; test++) {
            int n = 2 + random.nextInt(19);
            Graph graph = new Graph(n, false);

            // First create a connected spanning tree.
            for (int v = 1; v < n; v++) {
                int parent = random.nextInt(v);
                int weight = 1 + random.nextInt(1000);
                graph.addEdge(v, parent, weight);
            }

            // Add additional edges.
            int extraEdges = random.nextInt(n * 2);

            for (int i = 0; i < extraEdges; i++) {
                int u = random.nextInt(n);
                int v = random.nextInt(n);

                if (u != v) {
                    graph.addEdge(
                            u,
                            v,
                            1 + random.nextInt(1000)
                    );
                }
            }

            MSTResult kruskal = Kruskal.findMST(graph);
            MSTResult prim = Prim.findMST(graph);

            assertEquals(
                    kruskal.getTotalWeight(),
                    prim.getTotalWeight(),
                    "MST weights differ in random test " + test
            );

            assertEquals(n - 1, kruskal.getEdges().size());
            assertEquals(n - 1, prim.getEdges().size());

            assertEquals(1, kruskal.getComponents());
            assertEquals(1, prim.getComponents());
        }
    }

}