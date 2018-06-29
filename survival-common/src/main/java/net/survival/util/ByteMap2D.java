package net.survival.util;

public class ByteMap2D implements ByteSampler2D
{
    protected final byte[] map;
    
    public final int lengthX;
    public final int lengthZ;
    
    public ByteMap2D(int lengthX, int lengthZ) {
        map = new byte[lengthX * lengthZ];
        this.lengthX = lengthX;
        this.lengthZ = lengthZ;
    }
    
    public int getArea() {
        return lengthX * lengthZ;
    }
    
    protected int indexOf(int x, int z) {
        return x + z * lengthX;
    }
    
    @Override
    public byte sampleNearest(int x, int z) {
        return map[indexOf(x, z)];
    }
}