package org.pasqg.terraingen.utils;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public enum MathUtils {
    ;

    public static float[] minMaxScaling(float[] aArray) {
        float min = aArray[0];
        float max = aArray[0];
        float[] result = new float[aArray.length];
        for (int i = 0; i < aArray.length; i++) {
            min = Math.min(min, aArray[i]);
            max = Math.max(max, aArray[i]);
        }
        for (int i = 0; i < aArray.length; i++) {
            result[i] = (aArray[i] - min) / (max - min);
        }
        return result;
    }

    public static int[] generatePermutation(int aN, int aSeed) {
        Random random = new Random(aSeed);

        List<Integer> ints = new LinkedList<>();
        for (int i = 0; i < aN; i++) {
            ints.add(i);
        }

        int[] permutation = new int[aN];
        for (int i = 0; i < aN; i++) {
            int index = random.nextInt(ints.size());
            permutation[i] = ints.remove(index);
        }
        return permutation;
    }
}
