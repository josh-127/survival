package net.survival.gen;

class SurfaceLayer
{
    private final int blockId;
    private final int minThickness;
    private final int maxThickness;

    public SurfaceLayer(int blockId, int minThickness, int maxThickness) {
        this.blockId = blockId;
        this.minThickness = minThickness;
        this.maxThickness = maxThickness;
    }

    public int getBlockId() {
        return blockId;
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