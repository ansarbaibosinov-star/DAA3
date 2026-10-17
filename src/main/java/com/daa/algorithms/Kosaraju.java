package com.daa.algorithms;

import com.daa.graph.Edge;
import com.daa.graph.Graph;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

public final class Kosaraju {

    private Kosaraju() {
    }

    public static SCCResult findSCC(Graph graph) {
        if (!graph.isDirected()) {
            throw new IllegalArgumentException(
                    "Kosaraju requires a directed graph"
            );
        }

        int n = graph.getVertexCount();
        boolean[] visited = new boolean[n];
        int[] finishOrder = new int[n];
        int orderSize = 0;

        // First DFS: record vertices in finishing order.
        for (int start = 0; start < n; start++) {
            if (visited[start]) {
                continue;
            }

            Deque<DFSFrame> stack = new ArrayDeque<>();
            visited[start] = true;
            stack.push(new DFSFrame(start));

            while (!stack.isEmpty()) {
                DFSFrame frame = stack.peek();
                var edges = graph.getAdjacentEdges(frame.vertex);

                if (frame.nextEdgeIndex < edges.size()) {
                    Edge edge = edges.get(frame.nextEdgeIndex++);
                    int next = edge.getTo();

                    if (!visited[next]) {
                        visited[next] = true;
                        stack.push(new DFSFrame(next));
                    }
                } else {
                    finishOrder[orderSize++] = frame.vertex;
                    stack.pop();
                }
            }
        }

        // Second DFS: traverse the reversed graph in reverse finish order.
        Graph reversed = graph.reverse();
        Arrays.fill(visited, false);

        int[] componentOf = new int[n];
        Arrays.fill(componentOf, -1);
        int componentCount = 0;

        for (int i = orderSize - 1; i >= 0; i--) {
            int start = finishOrder[i];

            if (visited[start]) {
                continue;
            }

            Deque<Integer> stack = new ArrayDeque<>();
            stack.push(start);
            visited[start] = true;
            componentOf[start] = componentCount;

            while (!stack.isEmpty()) {
                int vertex = stack.pop();

                for (Edge edge : reversed.getAdjacentEdges(vertex)) {
                    int next = edge.getTo();

                    if (!visited[next]) {
                        visited[next] = true;
                        componentOf[next] = componentCount;
                        stack.push(next);
                    }
                }
            }

            componentCount++;
        }

        return new SCCResult(componentCount, componentOf);
    }

    private static final class DFSFrame {
        private final int vertex;
        private int nextEdgeIndex;

        private DFSFrame(int vertex) {
            this.vertex = vertex;
        }
    }
}