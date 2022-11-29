package org.pasqg.terraingen.utils;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public enum MathUtils {
    ;

    public static float[] normalizeModulo(float[] aVector) {
        float magnitude = magnitude(aVector);
        float[] result = new float[aVector.length];
        for (int i = 0; i < aVector.length; i++) {
            result[i] = aVector[i] / magnitude;
        }
        return result;
    }

    public static float[] addVectors(float[] aFirst, float[] aSecond) {
        float[] result = new float[aFirst.length];
        for (int i = 0; i < aFirst.length; i++) {
            result[i] = aFirst[i] + aSecond[i];
        }
        return result;
    }

    public static float[] subtractVectors(float[] aFirst, float[] aSecond) {
        float[] result = new float[aFirst.length];
        for (int i = 0; i < aFirst.length; i++) {
            result[i] = aFirst[i] - aSecond[i];
        }
        return result;
    }

    public static float magnitude(float[] aVector) {
        float sum = 0;
        for (int i = 0; i < aVector.length; i++) {
            sum += aVector[i] * aVector[i];
        }
        return (float) Math.sqrt(sum);
    }

    public static float[] copy(float[] aStartMap, int aSizeX, int aSizeY) {
        float[] newMap = new float[aSizeX * aSizeY];
        System.arraycopy(aStartMap, 0, newMap, 0, aSizeX * aSizeY);
        return newMap;
    }

    public static float[] vec2(float x, float y) {
        return new float[]{x, y};
    }

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
