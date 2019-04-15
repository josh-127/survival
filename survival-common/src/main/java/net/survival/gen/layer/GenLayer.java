package net.survival.gen.layer;

import java.util.Random;

import net.survival.util.ByteMap2D;
import net.survival.util.IntNoise;

public class GenLayer extends ByteMap2D
{
    public final long baseSeed;

    private int lastOffsetX;
    private int lastOffsetZ;

    public GenLayer(int lengthX, int lengthZ, long baseSeed) {
        super(lengthX, lengthZ);

        this.baseSeed = baseSeed;
    }

    protected Random rngFromPosition(Random random, int x, int z) {
        var columnSeed = (long) IntNoise.white2D(x, z, baseSeed);

        if (random == null) {
            random = new Random(columnSeed);
        }
        else {
            random.setSeed(columnSeed);
        }

        return random;
    }

    protected byte[] getMap() {
        return map;
    }

    public void generate(int offsetX, int offsetZ) {
        lastOffsetX = offsetX;
        lastOffsetZ = offsetZ;
    }

    public int getLastOffsetX() {
        return lastOffsetX;
    }

    public int getLastOffsetZ() {
        return lastOffsetZ;
    }
}