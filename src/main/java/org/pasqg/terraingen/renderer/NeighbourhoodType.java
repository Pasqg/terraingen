package org.pasqg.terraingen.renderer;

import java.util.List;

public enum NeighbourhoodType {
    VON_NEUMANN {
        private final List<int[]> VON_NEUMANN_INDEXES =
                List.of(new int[]{0, -1, 0}, new int[]{-1, 0, 0}, new int[]{1, 0, 0}, new int[]{0, 1, 0});

        @Override
        public int size() {
            return 4;
        }

        @Override
        public List<int[]> indexes(int aX, int aY) {
            return VON_NEUMANN_INDEXES;
        }
    },
    ;

    public abstract int size();

    public abstract List<int[]> indexes(int aX, int aY);
}
