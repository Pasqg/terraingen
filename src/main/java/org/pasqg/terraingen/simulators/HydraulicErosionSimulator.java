package org.pasqg.terraingen.simulators;

import org.pasqg.terraingen.parameters.ErosionParameters;
import org.pasqg.terraingen.utils.IndexCalculator;

import java.util.Random;

import static org.pasqg.terraingen.utils.MathUtils.addVectors;
import static org.pasqg.terraingen.utils.MathUtils.copy;
import static org.pasqg.terraingen.utils.MathUtils.magnitude;
import static org.pasqg.terraingen.utils.MathUtils.normalizeModulo;
import static org.pasqg.terraingen.utils.MathUtils.subtractVectors;
import static org.pasqg.terraingen.utils.MathUtils.vec2;

/**
 * This simulator simulates fluid erosion. The gist of fluid erosion is fluid particles moving ground matter from one
 * place to another.
 * <p>
 * Case 1 - RAIN) When a rain droplet drops, it will start flowing downhill, collecting material depending on its speed.
 * The starting speed depends on the atmosphere thickness and gravity -> terminal velocity.
 * Once it reaches the surface, it starts to slow down depending on viscosity and how much kinetic energy dissipates.
 * When it stops, it evaporates and deposits sediment.
 * <p>
 * Case 2 - RIVERS) Rivers collect sediment along their full course and transport it for long distances into the sea.
 * This means sediment doesn't collect at the end like for rain.
 * It will slowly carve canyons, until eventually it evaporates leaving the trails behind.
 * This will probably help creating canyon like structures in deserts
 * As the rivers carves the landscape, its flow might also change!
 * <p>
 * Case 3 - LAVA) Lava erosion is similar. Its particles will remove material due to temperature, however it flows
 * much slower and solidifies before reaching a flat region.
 * <p>
 * Case 4 - GLACIERS) They move immensely slow, but remove a lot of sediment, creating steep walls and smooth floors.
 * So they remove more sediment when it's more steep and less sediment when it is less steep.
 * Example, yosemite glacial valleys, fjords in northen regions etc (FJORDS are valleys invaded by the ocean!
 */
public enum HydraulicErosionSimulator {
    ;

    public static float[] erode(float[] aStartMap, ErosionParameters aErosionParameters) {
        float[] newMap = copy(aStartMap, aErosionParameters.getMapSizeX(), aErosionParameters.getMapSizeY());
        new RainErosionSimulator(aErosionParameters).erodeInPlace(newMap);
        return newMap;
    }

    private static class RainErosionSimulator {
        private final ErosionParameters mParameters;
        private final Random mRandom;

        RainErosionSimulator(ErosionParameters aErosionParameters) {
            mParameters = aErosionParameters;
            mRandom = new Random(aErosionParameters.getSeed());
        }

        public void erodeInPlace(float[] aStartMap) {
            for (int i = 0; i < mParameters.getParticlesNum(); i++) {
                dropParticle(aStartMap);
            }
        }

        private void dropParticle(float[] aHeightMap) {
            int sizeX = mParameters.getMapSizeX();
            int sizeY = mParameters.getMapSizeY();
            IndexCalculator index = IndexCalculator.withIndexWrapping(sizeX, sizeY);

            float[] position = new float[]{mRandom.nextFloat() * sizeX, mRandom.nextFloat() * sizeY};
            float[] direction = normalizeModulo(new float[]{mRandom.nextFloat(), mRandom.nextFloat()});

            // Biggest effect on erosion
            float speed = 10 * calculateTerminalVelocity();
            // Second biggest effect
            float mass = randomFromRange(mParameters.getMaxParticleMass(),
                    mParameters.getMinParticleMass());

            // Next effects
            final float unitCapacity = randomFromRange(mParameters.getParticleCapacity());
            final float inertia = randomFromRange(mParameters.getParticleInertia());

            float sediment = 0;
            int iterations = 0;
            while (speed > 0.001 && mass > 0.01 && iterations < Math.min(sizeX, sizeY)) {
                float time = 1 / speed;

                int x = (int) position[0];
                int y = (int) position[1];
                int indexOld = index.of(x, y);
                float heightOld = aHeightMap[indexOld];
                float[] gradient = bilinearGradient2D(index, aHeightMap, position);

                //more speed = more energy, thus more inertia
                direction = calculateDirection(direction, gradient, cap(inertia * speed));
                position = addVectors(position, direction);
                float heightNew = aHeightMap[index.of((int) position[0], (int) position[1])];
                float heightDiff = heightNew - heightOld;

                float deposited;
                float capacity =
                        Math.max(-heightDiff, mParameters.getMinSedimentPickup()) * speed * mass * unitCapacity;
                if (heightDiff > 0) {
                    deposited = Math.min(heightDiff, sediment);
                } else {
                    deposited = sediment > capacity
                            ? (sediment - capacity) * mParameters.getDepositionRate()
                            : Math.max((sediment - capacity) * mParameters.getErosionRate(), heightDiff);
                }
                sediment = floor(sediment - updateTerrain(index,
                        aHeightMap,
                        x,
                        y,
                        deposited,
                        mParameters.getDepositionRadius()));

                //evaporation depends only on time, which is distance / speed = 1 / speed;
                mass = floor(mass * (1 - mParameters.getEvaporationRate() * time));

                //the more sediment it has, the slower it gets, but the faster it accelerates when falling?
                speed = (float) Math.sqrt(floor(speed * speed + heightDiff * mParameters.getGravityAcceleration()
                        - mass * time * 0.05f));

                iterations++;
            }
        }

        private static float updateTerrain(IndexCalculator aIndex,
                float[] aMap,
                int aX,
                int aY,
                float aSediment,
                int aRadius) {
            if (aSediment > 0) {
                NeighbourhoodType neighbourhood = NeighbourhoodType.VON_NEUMANN;
                int size = neighbourhood.size() + 1;
                neighbourhood.indexes(aX, aY).forEach(aIndices -> {
                    int i = aIndex.of(aX + aIndices[0], aY + aIndices[1]);
                    aMap[i] = floor(cap(aMap[i] + aSediment / (size * size)));
                });
                aMap[aIndex.of(aX, aY)] =
                        cap(floor(aMap[aIndex.of(aX, aY)] + aSediment * size * (size - 1) / (size * size)));
            } else {
                float[] weights = new float[aRadius * aRadius];
                float wsum = 0;
                float[] origin = vec2(aX, aY);
                for (int i = 0; i < aRadius; i++) {
                    for (int k = 0; k < aRadius; k++) {
                        weights[i * aRadius + k] =
                                floor(aRadius - magnitude(subtractVectors(origin, vec2(aX - k, aY - i))));
                        wsum += weights[i * aRadius + k];
                    }
                }
                for (int i = 0; i < aRadius; i++) {
                    for (int k = 0; k < aRadius; k++) {
                        int index = aIndex.of(aX + k - aRadius / 2, aY + i - aRadius / 2);
                        aMap[index] = floor(cap(aMap[index] + aSediment * weights[i * aRadius + k] / wsum));
                    }
                }
            }
            return aSediment;
        }

        /**
         * Rain drops fall with a speed of about 1 to 10 m/s depending on the size.
         * This also depends on atmosphere density and gravity.
         * Terminal velocity is proportional to sqrt(gravity acceleration / density of atmosphere)
         */
        private float calculateTerminalVelocity() {
            float maxVelocity = mParameters.getMaxTerminalVelocity();
            float minVelocity = mParameters.getMinTerminalVelocity();
            return randomFromRange(maxVelocity, minVelocity);
        }

        private float randomFromRange(float aMax, float aMin) {
            return mRandom.nextFloat() * (aMax - aMin) + aMin;
        }

        private float randomFromRange(float aMax) {
            return mRandom.nextFloat() * aMax;
        }

        private float[] calculateDirection(float[] aDirection, float[] aGradient, float aInertia) {
            float[] newDirection = {
                    aDirection[0] * aInertia - aGradient[0] * (1 - aInertia),
                    aDirection[1] * aInertia - aGradient[1] * (1 - aInertia),
            };
            return magnitude(newDirection) > 0
                    ? normalizeModulo(newDirection)
                    : normalizeModulo(new float[]{mRandom.nextFloat(), mRandom.nextFloat()});
        }

        private static float[] bilinearGradient2D(IndexCalculator aIndex, float[] aField, float[] aPosition) {
            int x = (int) aPosition[0];
            int y = (int) aPosition[1];
            float u = aPosition[0] - x;
            float v = aPosition[1] - y;
            float topLeft = aField[aIndex.of(x, y)];
            float topRight = aField[aIndex.of(x + 1, y)];
            float bottomRight = aField[aIndex.of(x + 1, y + 1)];
            float bottomLeft = aField[aIndex.of(x, y + 1)];
            return new float[]{
                    (topRight - topLeft) * (1 - v) + (bottomRight - bottomLeft) * v,
                    (bottomLeft - topLeft) * (1 - u) + (bottomRight - topRight) * u,
            };
        }

        private static float floor(float x) {
            return Math.max(0, x);
        }

        private static float cap(float x) {
            return Math.min(1, x);
        }
    }
}
