package com.daa.algorithms;

public final class SCCResult {
    private final int componentCount;
    private final int[] componentOf;

    public SCCResult(int componentCount, int[] componentOf) {
        this.componentCount = componentCount;
        this.componentOf = componentOf.clone();
    }

    public int getComponentCount() {
        return componentCount;
    }

    public int getComponentOf(int vertex) {
        if (vertex < 0 || vertex >= componentOf.length) {
            throw new IndexOutOfBoundsException("Invalid vertex: " + vertex);
        }
        return componentOf[vertex];
    }

    public int[] getComponents() {
        return componentOf.clone();
    }
}