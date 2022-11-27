package org.pasqg.terraingen.noise;

import org.pasqg.terraingen.utils.MathUtils;

/**
 * From JAVA REFERENCE IMPLEMENTATION OF IMPROVED NOISE - COPYRIGHT 2002 KEN PERLIN.
 * <p>
 * See https://cs.nyu.edu/~perlin/noise/
 */
public class PerlinNoise {
    private final int[] mPermutation;

    public PerlinNoise(int aSeed) {
        mPermutation = new int[512];
        int[] permutation = MathUtils.generatePermutation(256, aSeed);
        for (int i = 0; i < 256; i++) {
            mPermutation[256 + i] = mPermutation[i] = permutation[i];
        }
    }

    public double noise2D(double x, double y) {
        int X = (int) Math.floor(x) & 255, Y = (int) Math.floor(y) & 255;
        x -= Math.floor(x); y -= Math.floor(y);
        double u = fade(x), v = fade(y);

        int A = mPermutation[X] + Y, AA = mPermutation[A], AB = mPermutation[A + 1];// HASH COORDINATES OF
        int B = mPermutation[X + 1] + Y, BA = mPermutation[B], BB = mPermutation[B + 1]; // THE 8 CUBE CORNERS,

        double z = 0;
        return lerp(v, lerp(u, grad(mPermutation[AA], x, y, z),  // AND ADD
                grad(mPermutation[BA], x - 1, y, z)), // BLENDED
                lerp(u, grad(mPermutation[AB], x, y - 1, z),  // RESULTS
                        grad(mPermutation[BB], x - 1, y - 1, z)));
    }

    public double noise3D(double x, double y, double z) {
        int X = (int) Math.floor(x) & 255,                  // FIND UNIT CUBE THAT
                Y = (int) Math.floor(y) & 255,                  // CONTAINS POINT.
                Z = (int) Math.floor(z) & 255;
        x -= Math.floor(x);                                // FIND RELATIVE X,Y,Z
        y -= Math.floor(y);                                // OF POINT IN CUBE.
        z -= Math.floor(z);
        double u = fade(x),                                // COMPUTE FADE CURVES
                v = fade(y),                                // FOR EACH OF X,Y,Z.
                w = fade(z);
        int A = mPermutation[X] + Y, AA = mPermutation[A] + Z, AB = mPermutation[A + 1] + Z;// HASH COORDINATES OF
        int B = mPermutation[X + 1] + Y, BA = mPermutation[B] + Z, BB = mPermutation[B + 1] + Z; // THE 8 CUBE CORNERS,

        return lerp(w, lerp(v, lerp(u, grad(mPermutation[AA], x, y, z),  // AND ADD
                grad(mPermutation[BA], x - 1, y, z)), // BLENDED
                lerp(u, grad(mPermutation[AB], x, y - 1, z),  // RESULTS
                        grad(mPermutation[BB], x - 1, y - 1, z))),// FROM  8
                lerp(v, lerp(u, grad(mPermutation[AA + 1], x, y, z - 1),  // CORNERS
                        grad(mPermutation[BA + 1], x - 1, y, z - 1)), // OF CUBE
                        lerp(u, grad(mPermutation[AB + 1], x, y - 1, z - 1),
                                grad(mPermutation[BB + 1], x - 1, y - 1, z - 1))));
    }

    private static double fade(double t) {
        return t * t * t * (t * (t * 6 - 15) + 10);
    }

    private static double lerp(double t, double a, double b) {
        return a + t * (b - a);
    }

    private static double grad(int hash, double x, double y, double z) {
        int h = hash & 15;                      // CONVERT LO 4 BITS OF HASH CODE
        double u = h < 8 ? x : y,                 // INTO 12 GRADIENT DIRECTIONS.
                v = h < 4 ? y : h == 12 || h == 14 ? x : z;
        return ((h & 1) == 0 ? u : -u) + ((h & 2) == 0 ? v : -v);
    }
}
