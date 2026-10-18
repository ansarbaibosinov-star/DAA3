package com.daa.algorithms;

public final class Metrics {

    private long dsuOps;
    private long edgeScans;
    private long comparisons;
    private int maxDepth;

    public void recordDsuOp() {
        dsuOps++;
    }

    public void recordEdgeScan() {
        edgeScans++;
    }

    public void recordComparison() {
        comparisons++;
    }

    public void recordDepth(int depth) {
        if (depth > maxDepth) {
            maxDepth = depth;
        }
    }

    public long getDsuOps() {
        return dsuOps;
    }

    public long getEdgeScans() {
        return edgeScans;
    }

    public long getComparisons() {
        return comparisons;
    }

    public int getMaxDepth() {
        return maxDepth;
    }

    public void reset() {
        dsuOps = 0;
        edgeScans = 0;
        comparisons = 0;
        maxDepth = 0;
    }
}