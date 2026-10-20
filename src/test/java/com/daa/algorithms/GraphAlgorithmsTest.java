package com.daa.algorithms;

import com.daa.graph.Edge;
import com.daa.graph.Graph;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GraphAlgorithmsTest {

    @Test
    void kosarajuShouldFindStronglyConnectedComponents() {
        Graph graph = new Graph(5, true);

        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 1);
        graph.addEdge(2, 0, 1);
        graph.addEdge(1, 3, 1);
        graph.addEdge(3, 4, 1);
        graph.addEdge(4, 3, 1);

        SCCResult result = Kosaraju.findSCC(graph);

        assertEquals(2, result.getCount());

        int[] componentOf = result.getComponentOf();

        assertEquals(componentOf[0], componentOf[1]);
        assertEquals(componentOf[1], componentOf[2]);
        assertEquals(componentOf[3], componentOf[4]);
        assertNotEquals(componentOf[0], componentOf[3]);
    }

    @Test
    void kosarajuShouldHandleIsolatedVertices() {
        Graph graph = new Graph(4, true);

        SCCResult result = Kosaraju.findSCC(graph);

        assertEquals(4, result.getCount());
    }

    @Test
    void kahnShouldReturnTopologicalOrderForDag() {
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
            assertTrue(position[edge.getFrom()] < position[edge.getTo()]);
        }
    }

    @Test
    void kahnShouldDetectCycle() {
        Graph graph = new Graph(3, true);

        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 1);
        graph.addEdge(2, 0, 1);

        KahnResult result = Kahn.topologicalSort(graph);

        assertFalse(result.isAcyclic());
    }

    @Test
    void dagShortestPathsShouldCalculateDistancesFromSources() {
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

        assertArrayEquals(
                new long[]{0, 3, 5, 6, 8},
                distances
        );
    }

    @Test
    void dagShortestPathsShouldSupportNegativeWeights() {
        Graph graph = new Graph(4, true);

        graph.addEdge(0, 1, 4);
        graph.addEdge(0, 2, 5);
        graph.addEdge(1, 2, -3);
        graph.addEdge(2, 3, 2);

        DAGShortestPathResult result =
                DAGShortestPath.findShortestPaths(graph);

        long[] distances = result.getDistances();

        assertArrayEquals(
                new long[]{0, 4, 1, 3},
                distances
        );
    }

    @Test
    void dagShortestPathsShouldRejectCycles() {
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
    void kruskalAndPrimShouldFindSameMstWeight() {
        Graph graph = new Graph(4, false);

        graph.addEdge(0, 1, 10);
        graph.addEdge(0, 2, 6);
        graph.addEdge(0, 3, 5);
        graph.addEdge(1, 3, 15);
        graph.addEdge(2, 3, 4);

        MSTResult kruskalResult = Kruskal.findMST(graph);
        MSTResult primResult = Prim.findMST(graph);

        assertEquals(
                kruskalResult.getTotalWeight(),
                primResult.getTotalWeight()
        );

        assertEquals(19L, kruskalResult.getTotalWeight());
        assertEquals(1, kruskalResult.getComponents());
        assertEquals(1, primResult.getComponents());
    }
}