package com.daa.algorithms;

public class DSU {

    private final int[] parent;
    private final int[] size;
    private final Metrics metrics;

    private int componentCount;
    private long findCalls;
    private long successfulUnionCalls;

    public DSU(int n) {
        this(n, null);
    }

    public DSU(int n, Metrics metrics) {
        if (n < 0) {
            throw new IllegalArgumentException(
                    "Number of elements cannot be negative"
            );
        }

        this.metrics = metrics;
        this.parent = new int[n];
        this.size = new int[n];
        this.componentCount = n;

        for (int i = 0; i < n; i++) {
            parent[i] = i;
            size[i] = 1;
        }
    }

    public static DSU makeSet(int n) {
        return new DSU(n);
    }

    public static DSU makeSet(int n, Metrics metrics) {
        return new DSU(n, metrics);
    }

    public int find(int x) {
        checkElement(x);

        findCalls++;
        recordDsuOperation();

        if (parent[x] != x) {
            parent[x] = find(parent[x]);
        }

        return parent[x];
    }

    public boolean union(int x, int y) {
        recordDsuOperation();

        int rootX = find(x);
        int rootY = find(y);

        if (rootX == rootY) {
            return false;
        }

        // Union by size: attach the smaller tree to the larger tree.
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

    private void recordDsuOperation() {
        if (metrics != null) {
            metrics.recordDsuOp();
        }
    }

    private void checkElement(int x) {
        if (x < 0 || x >= parent.length) {
            throw new IndexOutOfBoundsException(
                    "Invalid element: " + x
            );
        }
    }
}