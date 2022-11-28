package org.pasqg.terraingen.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class IndexCalculatorTest {
    private static final int SIZE_Y = 20;
    private static final int SIZE_X = 10;

    @Test
    public void shouldNotWrapIndexes() {
        IndexCalculator index = IndexCalculator.noIndexWrapping(SIZE_X);

        assertEquals(index.of(5, 0), 5);
        assertEquals(index.of(5, 2), 5 + 2 * SIZE_X);
        assertEquals(index.of(13, 14), 13 + 14 * SIZE_X);
        assertEquals(index.of(100, 100), 100 + 100 * SIZE_X);
    }

    @Test
    public void shouldWrapIndexesAround() {
        IndexCalculator index = IndexCalculator.withIndexWrapping(SIZE_X, SIZE_Y);

        assertEquals(index.of(5, 0), 5);
        assertEquals(index.of(5, 2), 5 + 2 * SIZE_X);
        assertEquals(index.of(13, 14), 3 + 14 * SIZE_X);
        assertEquals(index.of(7, 25), 7 + 5 * SIZE_X);
        assertEquals(index.of(-43, 25), 7 + 5 * SIZE_X);
        assertEquals(index.of(-43, -25), 7 + 15 * SIZE_X);
        assertEquals(index.of(-10, 2), 0 + 2 * SIZE_X);
        assertEquals(index.of(-9, 2), 1 + 2 * SIZE_X);
        assertEquals(index.of(-11, 2), 9 + 2 * SIZE_X);
    }

}