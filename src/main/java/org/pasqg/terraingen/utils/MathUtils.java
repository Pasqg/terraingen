package org.pasqg.terraingen.utils;

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
}
