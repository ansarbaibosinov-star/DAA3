package com.daa.algorithms;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MetricsTest {

    @Test
    void countersStartAtZero() {
        Metrics metrics = new Metrics();

        assertEquals(0, metrics.getDsuOps());
        assertEquals(0, metrics.getEdgeScans());
        assertEquals(0, metrics.getComparisons());
        assertEquals(0, metrics.getMaxDepth());
    }

    @Test
    void countersIncreaseCorrectly() {
        Metrics metrics = new Metrics();

        metrics.recordDsuOp();
        metrics.recordDsuOp();

        metrics.recordEdgeScan();
        metrics.recordEdgeScan();
        metrics.recordEdgeScan();

        metrics.recordComparison();

        assertEquals(2, metrics.getDsuOps());
        assertEquals(3, metrics.getEdgeScans());
        assertEquals(1, metrics.getComparisons());
    }

    @Test
    void keepsMaximumDepth() {
        Metrics metrics = new Metrics();

        metrics.recordDepth(2);
        metrics.recordDepth(7);
        metrics.recordDepth(4);

        assertEquals(7, metrics.getMaxDepth());
    }

    @Test
    void resetClearsAllCounters() {
        Metrics metrics = new Metrics();

        metrics.recordDsuOp();
        metrics.recordEdgeScan();
        metrics.recordComparison();
        metrics.recordDepth(10);

        metrics.reset();

        assertEquals(0, metrics.getDsuOps());
        assertEquals(0, metrics.getEdgeScans());
        assertEquals(0, metrics.getComparisons());
        assertEquals(0, metrics.getMaxDepth());
    }
}