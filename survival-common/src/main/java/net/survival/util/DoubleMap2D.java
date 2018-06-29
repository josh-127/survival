package net.survival.util;

import static net.survival.util.MathEx.lerp;

public class DoubleMap2D implements DoubleSampler2D
{
    protected final double[] map;
    
    public final int lengthX;
    public final int lengthZ;
    
    public DoubleMap2D(int lengthX, int lengthZ) {
        map = new double[lengthX * lengthZ];
        this.lengthX = lengthX;
        this.lengthZ = lengthZ;
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
        double floorX   = Math.floor(x);
        double floorZ   = Math.floor(z);
        int leftPos     = (int) floorX;
        int rightPos    = leftPos + 1;
        int topPos      = (int) floorZ;
        int bottomPos   = topPos + 1;
        int topIndex    = topPos * lengthX;
        int bottomIndex = bottomPos * lengthX;
        
        double tl = map[leftPos  + topIndex];
        double tr = map[rightPos + topIndex];
        double bl = map[leftPos  + bottomIndex];
        double br = map[rightPos + bottomIndex];
        
        double fracX = x - floorX;
        double fracZ = z - floorZ;
        
        return lerp(lerp(tl, tr, fracX),
                    lerp(bl, br, fracX),
                    fracZ);
    }
}