
package com.daa.algorithms;

import com.daa.graph.Graph;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KosarajuTest {

    @Test
    void countsStronglyConnectedComponents() {
        Graph graph = new Graph(9, true);

        // Component 1: vertices 0 and 1
        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 0, 1);

        // Component 2: vertices 2, 3, 4 and 5
        graph.addEdge(1, 2, 1);
        graph.addEdge(2, 3, 1);
        graph.addEdge(3, 2, 1);
        graph.addEdge(3, 4, 1);
        graph.addEdge(4, 5, 1);
        graph.addEdge(5, 3, 1);

        // Component 3: vertices 6 and 7
        graph.addEdge(5, 6, 1);
        graph.addEdge(6, 7, 1);
        graph.addEdge(7, 6, 1);

        // Vertex 8 is isolated.
        SCCResult result = Kosaraju.findSCC(graph);

        assertEquals(4, result.getCount());
    }

    @Test
    void everyVertexGetsAComponent() {
        Graph graph = new Graph(5, true);

        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 0, 1);
        graph.addEdge(1, 2, 1);
        graph.addEdge(2, 3, 1);
        graph.addEdge(3, 2, 1);

        SCCResult result = Kosaraju.findSCC(graph);

        int[] componentOf = result.getComponentOf();

        assertEquals(5, componentOf.length);

        for (int component : componentOf) {
            assertTrue(component >= 0);
            assertTrue(component < result.getCount());
        }
    }

    @Test
    void handlesGraphWithOnlyIsolatedVertices() {
        Graph graph = new Graph(5, true);

        SCCResult result = Kosaraju.findSCC(graph);

        assertEquals(5, result.getCount());
    }

    @Test
    void handlesEmptyGraph() {
        Graph graph = new Graph(0, true);

        SCCResult result = Kosaraju.findSCC(graph);

        assertEquals(0, result.getCount());
        assertEquals(0, result.getComponentOf().length);
    }

    @Test
    void rejectsUndirectedGraph() {
        Graph graph = new Graph(2, false);
        graph.addEdge(0, 1, 1);

        assertThrows(
                IllegalArgumentException.class,
                () -> Kosaraju.findSCC(graph)
        );
    }

    @Test
    void handlesLargeGraphWithoutRecursiveDfs() {
        int n = 100_000;
        Graph graph = new Graph(n, true);

        for (int i = 0; i < n - 1; i++) {
            graph.addEdge(i, i + 1, 1);
        }

        SCCResult result = Kosaraju.findSCC(graph);

        assertEquals(n, result.getCount());
        assertEquals(n, result.getComponentOf().length);
    }

    @Test
    void updatesMetrics() {
        Graph graph = new Graph(3, true);

        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 1);

        Metrics metrics = new Metrics();

        SCCResult result = Kosaraju.findSCC(graph, metrics);

        assertEquals(3, result.getCount());
        assertTrue(metrics.getEdgeScans() > 0);
        assertTrue(metrics.getComparisons() > 0);
    }
}
