package net.survival.util;

import static net.survival.util.MathEx.lerp;

public class DoubleMap3D implements DoubleSampler3D
{
    protected final double[] map;
    private final int lengthX;
    private final int lengthY;
    private final int lengthZ;

    public DoubleMap3D(int lengthX, int lengthY, int lengthZ) {
        map = new double[lengthX * lengthY * lengthZ];
        this.lengthX = lengthX;
        this.lengthY = lengthY;
        this.lengthZ = lengthZ;
    }

    public double[] getRawMap() {
        return map;
    }

    public int getLengthX() {
        return lengthX;
    }

    public int getLengthY() {
        return lengthY;
    }

    public int getLengthZ() {
        return lengthZ;
    }

    public int getBaseArea() {
        return lengthX * lengthZ;
    }

    public int getVolume() {
        return lengthX * lengthY * lengthZ;
    }

    public void setPoint(int x, int y, int z, double to) {
        map[indexOf(x, y, z)] = to;
    }

    protected int indexOf(int x, int y, int z) {
        return x + (z * lengthX) + (y * lengthX * lengthZ);
    }

    @Override
    public double sampleNearest(int x, int y, int z) {
        return map[indexOf(x, y, z)];
    }

    @Override
    public double sampleLinear(double x, double y, double z) {
        var baseArea = getBaseArea();

        var floorX      = Math.floor(x);
        var floorY      = Math.floor(y);
        var floorZ      = Math.floor(z);
        var left        = (int) floorX;
        var bottom      = (int) floorY;
        var back        = (int) floorZ;
        var right       = left + 1;
        var top         = bottom + 1;
        var front       = back + 1;
        var backIndex   = back * lengthX;
        var frontIndex  = front * lengthX;
        var bottomIndex = bottom * baseArea;
        var topIndex    = top * baseArea;

        var bbl = map[left  + backIndex  + bottomIndex];
        var bbr = map[right + backIndex  + bottomIndex];
        var bfl = map[left  + frontIndex + bottomIndex];
        var bfr = map[right + frontIndex + bottomIndex];
        var tbl = map[left  + backIndex  + topIndex];
        var tbr = map[right + backIndex  + topIndex];
        var tfl = map[left  + frontIndex + topIndex];
        var tfr = map[right + frontIndex + topIndex];

        var fracX = x - floorX;
        var fracY = y - floorY;
        var fracZ = z - floorZ;

        return
            lerp(
                lerp(
                    lerp(bbl, bbr, fracX),
                    lerp(bfl, bfr, fracX),
                    fracZ),
                lerp(
                    lerp(tbl, tbr, fracX),
                    lerp(tfl, tfr, fracX),
                    fracZ),
                fracY);
    }
}