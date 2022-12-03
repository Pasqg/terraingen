package org.pasqg.terraingen.renderer;

import org.pasqg.terraingen.noise.NoiseGenerator;
import org.pasqg.terraingen.parameters.ErosionParameters;
import org.pasqg.terraingen.simulators.HydraulicErosionSimulator;
import org.pasqg.terraingen.utils.MathUtils;

import java.util.Random;

import static org.pasqg.terraingen.utils.MathUtils.addVectors;
import static org.pasqg.terraingen.utils.MathUtils.applyTransformation;
import static org.pasqg.terraingen.utils.MathUtils.minMaxScaling;

public class Landscaper {
    private long mSeed;
    private int mWidth;
    private int mHeight;
    private float[] mTerrain;

    /**
     * Generates a random terrain.
     * Scale defines the scale of the terrain features.
     * DetailLevel [0-1] defines the number of harmonics (0 = 1 harmonic, 1 = max harmonics)
     */
    public static Landscaper generateRandom(long aSeed, int aWidth, int aHeight, float aScale, float aDetailLevel) {
        Random random = new Random(aSeed);
        NoiseGenerator generator = new NoiseGenerator(random.nextInt());

        float[] base1 = generator.harmonicPerlin(aWidth, aHeight, aScale, 1);
        float[] base2 =
                generator.harmonicPerlin(aWidth,
                        aHeight,
                        aScale + 3.0f / aScale,
                        (int) (aDetailLevel * (32 - aScale - 2.0f)));

        return new Landscaper(aSeed, aWidth, aHeight, minMaxScaling(addVectors(base1, base2)));
    }

    private Landscaper(long aSeed, int aWidth, int aHeight, float[] aBase) {
        mSeed = aSeed;
        mWidth = aWidth;
        mHeight = aHeight;
        mTerrain = aBase;
    }

    public Landscaper mesas(float aMinHeight, float aLevels) {
        Random random = new Random(mSeed);
        NoiseGenerator generator = new NoiseGenerator(random.nextInt());
        float[] noise = generator.harmonicPerlin(mWidth, mHeight, 1, 1);
        noise = applyTransformation(noise,
                aNoise -> aMinHeight + discretize(aNoise,
                        aLevels) * (1.0f - aMinHeight) + (float) Math.random() * 0.02f);
        mTerrain = minMaxScaling(applyTransformation(mTerrain, noise, Math::min));
        return this;
    }

    public Landscaper invert() {
        mTerrain = applyTransformation(mTerrain, aV -> 1.0f - aV);
        return this;
    }

    public Landscaper smoothErosion() {
        ErosionParameters parameters = new ErosionParameters(mSeed)
                .setMapSizeX(mWidth)
                .setMapSizeY(mHeight)
                .setParticleInertia(0.003f)
                .setDepositionRadius(3)
                .setParticlesNum(200000);
        return runoffErosion(parameters);
    }

    public Landscaper harshErosion() {
        ErosionParameters parameters = new ErosionParameters(mSeed)
                .setMapSizeX(mWidth)
                .setMapSizeY(mHeight)
                .setParticleInertia(0.001f)
                .setDepositionRadius(2)
                .setParticlesNum(120000);
        return runoffErosion(parameters);
    }

    public Landscaper runoffErosion(ErosionParameters aErosionParameters) {
        mTerrain = HydraulicErosionSimulator.erode(mTerrain, aErosionParameters);
        return this;
    }

    public float[] terrain() {
        return mTerrain;
    }

    private static float discretize(float aValue, float aLevels) {
        return (float) Math.floor(aValue * aLevels) / aLevels;
    }
}
