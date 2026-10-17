package com.daa.algorithms;

public final class KahnResult {
    private final int[] order;
    private final boolean acyclic;

    public KahnResult(int[] order, boolean acyclic) {
        this.order = order.clone();
        this.acyclic = acyclic;
    }

    public int[] getOrder() {
        return order.clone();
    }

    public boolean isAcyclic() {
        return acyclic;
    }
}
