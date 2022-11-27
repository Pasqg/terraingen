package org.pasqg.terraingen.noise;

import org.pasqg.terraingen.utils.IndexCalculator;

import java.util.Random;

import static org.pasqg.terraingen.utils.MathUtils.minMaxScaling;

public class NoiseGenerator {
    private final Random mRandom;
    private final int mSeed;

    public NoiseGenerator(int aSeed) {
        mSeed = aSeed;
        mRandom = new Random(mSeed);
    }

    /**
     * Sums different harmonics of perlin noise
     */
    public float[] harmonicPerlin(int aSizeX, int aSizeY, int aStartHarmonic, int aHarmonics) {
        PerlinNoise perlin = new PerlinNoise(mSeed);
        IndexCalculator index = IndexCalculator.withIndexWrapping(aSizeX, aSizeY);
        float[] data = new float[aSizeX * aSizeY];
        for (int h = aStartHarmonic; h < harmonicNumberCap(aStartHarmonic, aHarmonics); h++) {
            double step = Math.pow(2, h - 1) / Math.min(aSizeX, aSizeY);
            double amplitude = 1 / Math.pow(2, h - 1);
            double offsetX = mRandom.nextDouble();
            double offsetY = mRandom.nextDouble();
            for (int i = 0; i < aSizeX; i++) {
                for (int k = 0; k < aSizeY; k++) {
                    data[index.of(i, k)] +=
                            (float) (perlin.noise2D(offsetX + k * step, offsetY + i * step) * amplitude);
                }
            }
        }
        return minMaxScaling(data);
    }

    private int harmonicNumberCap(int aStartHarmonic, int aHarmonics) {
        return Math.min(aHarmonics + aStartHarmonic, 32);
    }
}
