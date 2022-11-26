package org.pasqg.terraingen.utils;

public interface IndexCalculator {
    int of(int aX, int aY);

    static IndexCalculator withIndexWrapping(int aSizeX, int aSizeY) {
        return new ToroidalIndexCalculator(aSizeX, aSizeY);
    }

    class NoWrappingCalculator implements IndexCalculator {
        private final int mSizeX;

        private NoWrappingCalculator(int aSizeX) {
            mSizeX = aSizeX;
        }

        @Override
        public int of(int aX, int aY) {
            return project(aX, aY, mSizeX);
        }

        private static int project(int aX, int aY, int aSizeX) {
            return aY * aSizeX + aX;
        }
    }

    class ToroidalIndexCalculator implements IndexCalculator {
        private final int mSizeX;
        private final int mSizeY;

        private ToroidalIndexCalculator(int aSizeX, int aSizeY) {
            mSizeX = aSizeX;
            mSizeY = aSizeY;
        }

        @Override
        public int of(int aX, int aY) {
            return NoWrappingCalculator.project(toroidalWrapping(aX, mSizeX), toroidalWrapping(aY, mSizeY), mSizeX);
        }

        private static int toroidalWrapping(int aIndex, int aDimension) {
            int mod = aIndex % aDimension;
            return mod < 0
                    ? mod + aDimension
                    : mod;
        }
    }
}
