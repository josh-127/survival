package net.survival.util;

import static net.survival.util.MathEx.lerp;

public class DoubleMap2D implements DoubleSampler2D
{
    protected final double[] map;
    private final int lengthX;
    private final int lengthZ;

    public DoubleMap2D(int lengthX, int lengthZ) {
        map = new double[lengthX * lengthZ];
        this.lengthX = lengthX;
        this.lengthZ = lengthZ;
    }

    public double[] getRawMap() {
        return map;
    }

    public int getLengthX() {
        return lengthX;
    }

    public int getLengthZ() {
        return lengthZ;
    }

    public int getArea() {
        return lengthX * lengthZ;
    }

    public void setPoint(int x, int z, double to) {
        map[indexOf(x, z)] = to;
    }

    protected int indexOf(int x, int z) {
        return x + z * lengthX;
    }

    @Override
    public double sampleNearest(int x, int z) {
        return map[indexOf(x, z)];
    }

    @Override
    public double sampleLinear(double x, double z) {
        var floorX      = Math.floor(x);
        var floorZ      = Math.floor(z);
        var leftPos     = (int) floorX;
        var rightPos    = leftPos + 1;
        var topPos      = (int) floorZ;
        var bottomPos   = topPos + 1;
        var topIndex    = topPos * lengthX;
        var bottomIndex = bottomPos * lengthX;

        var tl = map[leftPos  + topIndex];
        var tr = map[rightPos + topIndex];
        var bl = map[leftPos  + bottomIndex];
        var br = map[rightPos + bottomIndex];

        var fracX = x - floorX;
        var fracZ = z - floorZ;

        return lerp(lerp(tl, tr, fracX), lerp(bl, br, fracX), fracZ);
    }
}