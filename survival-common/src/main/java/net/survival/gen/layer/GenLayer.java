package net.survival.gen.layer;

import net.survival.util.ByteMap2D;

public abstract class GenLayer extends ByteMap2D
{
    public final long baseSeed;

    public GenLayer(int lengthX, int lengthZ, long baseSeed) {
        super(lengthX, lengthZ);
        this.baseSeed = baseSeed;
    }

    protected byte[] getMap() {
        return map;
    }

    public abstract void generate(int offsetX, int offsetZ);
}