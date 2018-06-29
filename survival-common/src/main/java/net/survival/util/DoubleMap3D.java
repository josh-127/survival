package net.survival.util;

import static net.survival.util.MathEx.lerp;

public class DoubleMap3D implements DoubleSampler3D
{
    protected final double[] map;
    
    public final int lengthX;
    public final int lengthY;
    public final int lengthZ;
    
    public DoubleMap3D(int lengthX, int lengthY, int lengthZ) {
        map = new double[lengthX * lengthY * lengthZ];
        this.lengthX = lengthX;
        this.lengthY = lengthY;
        this.lengthZ = lengthZ;
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
        int baseArea = getBaseArea();
        
        double floorX   = Math.floor(x);
        double floorY   = Math.floor(y);
        double floorZ   = Math.floor(z);
        int left        = (int) floorX;
        int bottom      = (int) floorY;
        int back        = (int) floorZ;
        int right       = left + 1;
        int top         = bottom + 1;
        int front       = back + 1;
        int backIndex   = back * lengthX;
        int frontIndex  = front * lengthX;
        int bottomIndex = bottom * baseArea;
        int topIndex    = top * baseArea;
        
        double bbl = map[left  + backIndex  + bottomIndex];
        double bbr = map[right + backIndex  + bottomIndex];
        double bfl = map[left  + frontIndex + bottomIndex];
        double bfr = map[right + frontIndex + bottomIndex];
        double tbl = map[left  + backIndex  + topIndex];
        double tbr = map[right + backIndex  + topIndex];
        double tfl = map[left  + frontIndex + topIndex];
        double tfr = map[right + frontIndex + topIndex];
        
        double fracX = x - floorX;
        double fracY = y - floorY;
        double fracZ = z - floorZ;
        
        return lerp(lerp(lerp(bbl, bbr, fracX),
                         lerp(bfl, bfr, fracX),
                         fracZ),
                    lerp(lerp(tbl, tbr, fracX),
                         lerp(tfl, tfr, fracX),
                         fracZ),
                    fracY);
    }
}