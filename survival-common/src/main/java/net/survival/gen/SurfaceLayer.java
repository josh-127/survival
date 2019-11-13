package net.survival.gen;

import net.survival.block.state.BlockState;

public class SurfaceLayer
{
    private final BlockState block;
    private final int minThickness;
    private final int maxThickness;

    public SurfaceLayer(BlockState block, int minThickness, int maxThickness) {
        this.block = block;
        this.minThickness = minThickness;
        this.maxThickness = maxThickness;
    }

    public BlockState getBlock() {
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