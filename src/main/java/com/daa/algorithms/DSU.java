package com.daa.algorithms;

public class DSU {
    private final int[] parent;
    private final int[] size;

    private int componentCount;
    private long findCalls;
    private long successfulUnionCalls;

    public DSU(int n) {
        if (n < 0) {
            throw new IllegalArgumentException(
                    "Number of elements cannot be negative"
            );
        }

        parent = new int[n];
        size = new int[n];
        componentCount = n;

        for (int i = 0; i < n; i++) {
            parent[i] = i;
            size[i] = 1;
        }
    }

    public static DSU makeSet(int n) {
        return new DSU(n);
    }

    public int find(int x) {
        checkElement(x);
        findCalls++;

        // Path compression.
        if (parent[x] != x) {
            parent[x] = find(parent[x]);
        }

        return parent[x];
    }

    public boolean union(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);

        // Already in the same component.
        if (rootX == rootY) {
            return false;
        }

        // Union by size: attach the smaller tree to the larger one.
        if (size[rootX] < size[rootY]) {
            int temp = rootX;
            rootX = rootY;
            rootY = temp;
        }

        parent[rootY] = rootX;
        size[rootX] += size[rootY];

        componentCount--;
        successfulUnionCalls++;

        return true;
    }

    public int components() {
        return componentCount;
    }

    public long getFindCalls() {
        return findCalls;
    }

    public long getSuccessfulUnionCalls() {
        return successfulUnionCalls;
    }

    private void checkElement(int x) {
        if (x < 0 || x >= parent.length) {
            throw new IndexOutOfBoundsException(
                    "Invalid element: " + x
            );
        }
    }
}