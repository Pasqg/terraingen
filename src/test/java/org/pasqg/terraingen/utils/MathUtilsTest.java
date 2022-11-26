package org.pasqg.terraingen.utils;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.pasqg.terraingen.utils.MathUtils.generatePermutation;

public class MathUtilsTest {

    @Test
    public void testPermutation2() {
        assertEquals(List.of(1, 0), generate(2, 0));
        assertEquals(List.of(0, 1), generate(2, 2942497));
    }

    @Test
    public void testPermutation3() {
        assertEquals(List.of(0, 2, 1), generate(3, 0));
        assertEquals(List.of(0, 1, 2), generate(3, 1));
        assertEquals(List.of(1, 0, 2), generate(3, 2));
        assertEquals(List.of(2, 1, 0), generate(3, 3));
        assertEquals(List.of(2, 0, 1), generate(3, 5));
        assertEquals(List.of(1, 2, 0), generate(3, 7));
    }

    private List<Integer> generate(int aAI, int aAI2) {
        return Arrays.stream(generatePermutation(aAI, aAI2)).boxed().collect(Collectors.toList());
    }
}