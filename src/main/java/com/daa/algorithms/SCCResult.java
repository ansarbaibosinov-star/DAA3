package com.daa.algorithms;

public class SCCResult {

    private final int count;
    private final int[] componentOf;

    public SCCResult(int count, int[] componentOf) {
        this.count = count;
        this.componentOf = componentOf.clone();
    }

    public int getCount() {
        return count;
    }

    public int[] getComponentOf() {
        return componentOf.clone();
    }
}
