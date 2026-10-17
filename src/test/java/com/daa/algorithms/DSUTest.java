package com.daa.algorithms;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class DSUTest {

    @Test
    void shouldCreateSeparateSets() {
        DSU dsu = DSU.makeSet(5);

        assertEquals(5, dsu.components());

        for (int i = 0; i < 5; i++) {
            assertEquals(i, dsu.find(i));
        }
    }

    @Test
    void shouldMergeTwoDifferentSets() {
        DSU dsu = new DSU(4);

        assertTrue(dsu.union(0, 1));
        assertEquals(3, dsu.components());
        assertEquals(dsu.find(0), dsu.find(1));
    }

    @Test
    void shouldNotMergeAlreadyConnectedElements() {
        DSU dsu = new DSU(3);

        assertTrue(dsu.union(0, 1));
        assertFalse(dsu.union(0, 1));

        assertEquals(2, dsu.components());
        assertEquals(1, dsu.getSuccessfulUnionCalls());
    }

    @Test
    void shouldConnectElementsTransitively() {
        DSU dsu = new DSU(5);

        dsu.union(0, 1);
        dsu.union(1, 2);
        dsu.union(3, 4);

        assertEquals(dsu.find(0), dsu.find(2));
        assertEquals(dsu.find(3), dsu.find(4));
        assertNotEquals(dsu.find(0), dsu.find(3));
        assertEquals(2, dsu.components());
    }

    @Test
    void shouldTrackOperationCounters() {
        DSU dsu = new DSU(3);

        dsu.union(0, 1);
        dsu.union(0, 1);
        dsu.find(2);

        assertTrue(dsu.getFindCalls() >= 5);
        assertEquals(1, dsu.getSuccessfulUnionCalls());
    }

    @Test
    void shouldHandleEmptyDsu() {
        DSU dsu = new DSU(0);

        assertEquals(0, dsu.components());
        assertEquals(0, dsu.getFindCalls());
        assertEquals(0, dsu.getSuccessfulUnionCalls());
    }

    @Test
    void shouldRejectInvalidElements() {
        DSU dsu = new DSU(3);

        assertThrows(
                IndexOutOfBoundsException.class,
                () -> dsu.find(-1)
        );

        assertThrows(
                IndexOutOfBoundsException.class,
                () -> dsu.find(3)
        );

        assertThrows(
                IndexOutOfBoundsException.class,
                () -> dsu.union(0, 3)
        );
    }

    @Test
    void shouldRejectNegativeSize() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new DSU(-1)
        );
    }

    @Test
    void shouldMatchSimpleReferenceImplementation() {
        int n = 50;
        DSU dsu = new DSU(n);

        // The reference implementation uses simple component labels.
        int[] label = new int[n];

        for (int i = 0; i < n; i++) {
            label[i] = i;
        }

        Random random = new Random(42);

        for (int step = 0; step < 500; step++) {
            int a = random.nextInt(n);
            int b = random.nextInt(n);

            boolean expectedMerge = label[a] != label[b];
            boolean actualMerge = dsu.union(a, b);

            assertEquals(expectedMerge, actualMerge);

            if (expectedMerge) {
                int oldLabel = label[b];
                int newLabel = label[a];

                for (int i = 0; i < n; i++) {
                    if (label[i] == oldLabel) {
                        label[i] = newLabel;
                    }
                }
            }

            assertEquals(
                    countDistinctLabels(label),
                    dsu.components()
            );

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    boolean expectedSame = label[i] == label[j];
                    boolean actualSame = dsu.find(i) == dsu.find(j);

                    assertEquals(expectedSame, actualSame);
                }
            }
        }
    }

    private int countDistinctLabels(int[] labels) {
        int count = 0;

        for (int i = 0; i < labels.length; i++) {
            boolean seenBefore = false;

            for (int j = 0; j < i; j++) {
                if (labels[i] == labels[j]) {
                    seenBefore = true;
                    break;
                }
            }

            if (!seenBefore) {
                count++;
            }
        }

        return count;
    }
}
