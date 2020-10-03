package net.survival.gen;

import net.survival.block.Block;

public class SurfaceLayer {
    private final Block block;
    private final int minThickness;
    private final int maxThickness;

    public SurfaceLayer(Block block, int minThickness, int maxThickness) {
        this.block = block;
        this.minThickness = minThickness;
        this.maxThickness = maxThickness;
    }

    public Block getBlock() {
        return block;
    }

    public int getMinThickness() {
        return minThickness;
    }

    public int getMaxThickness() {
        return maxThickness;
    }

    public int getThicknessRange() {
        return maxThickness - minThickness + 1;
    }
}