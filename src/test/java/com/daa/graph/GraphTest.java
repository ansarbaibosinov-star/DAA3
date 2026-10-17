package com.daa.graph;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GraphTest {


    @Test
    void shouldCreateGraphWithIsolatedVertices() {
        Graph graph = new Graph(4, false);

        assertEquals(4, graph.getVertexCount());
        assertEquals(0, graph.getEdgeCount());
        assertEquals(0, graph.getOutDegree(2));
    }

    @Test
    void shouldStoreUndirectedEdgeInBothDirections() {
        Graph graph = new Graph(3, false);
        graph.addEdge(0, 1, 10);

        assertEquals(1, graph.getEdgeCount());
        assertEquals(1, graph.getOutDegree(0));
        assertEquals(1, graph.getOutDegree(1));

        Edge forward = graph.getAdjacentEdges(0).get(0);
        Edge backward = graph.getAdjacentEdges(1).get(0);

        assertEquals(1, forward.getTo());
        assertEquals(0, backward.getTo());
        assertEquals(10, backward.getWeight());
    }

    @Test
    void shouldStoreDirectedEdgeInOneDirection() {
        Graph graph = new Graph(3, true);
        graph.addEdge(0, 1, 7);

        assertEquals(1, graph.getEdgeCount());
        assertEquals(1, graph.getOutDegree(0));
        assertEquals(0, graph.getOutDegree(1));
        assertEquals(1, graph.getInDegree(1));
        assertEquals(0, graph.getInDegree(0));
    }

    @Test
    void shouldAllowSelfLoopsAndParallelEdges() {
        Graph graph = new Graph(2, true);

        graph.addEdge(0, 0, 5);
        graph.addEdge(0, 1, 10);
        graph.addEdge(0, 1, 20);

        assertEquals(3, graph.getEdgeCount());
        assertEquals(3, graph.getOutDegree(0));
        assertEquals(1, graph.getInDegree(0));
        assertEquals(2, graph.getInDegree(1));
    }

    @Test
    void shouldReverseDirectedGraph() {
        Graph graph = new Graph(3, true);
        graph.addEdge(0, 1, 8);
        graph.addEdge(1, 2, 4);

        Graph reversed = graph.reverse();

        assertEquals(2, reversed.getEdgeCount());
        assertEquals(1, reversed.getOutDegree(2));
        assertEquals(1, reversed.getOutDegree(1));
        assertEquals(0, reversed.getOutDegree(0));

        Edge edge = reversed.getAdjacentEdges(2).get(0);
        assertEquals(1, edge.getTo());
        assertEquals(4, edge.getWeight());
    }

    @Test
    void shouldRejectInvalidVertex() {
        Graph graph = new Graph(3, false);

        assertThrows(
                IndexOutOfBoundsException.class,
                () -> graph.addEdge(0, 3, 10)
        );

        assertThrows(
                IndexOutOfBoundsException.class,
                () -> graph.getAdjacentEdges(-1)
        );
    }

    @Test
    void shouldRejectNegativeVertexCount() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Graph(-1, false)
        );
    }

    @Test
    void returnedEdgeListsShouldNotBeModifiable() {
        Graph graph = new Graph(2, true);
        graph.addEdge(0, 1, 5);

        List<Edge> edgeList = graph.getEdges();

        assertThrows(
                UnsupportedOperationException.class,
                () -> edgeList.clear()
        );
    }

}
