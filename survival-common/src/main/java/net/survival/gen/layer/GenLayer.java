package net.survival.gen.layer;

import java.util.ArrayList;
import java.util.Random;

import net.survival.util.ByteMap2D;
import net.survival.util.IntNoise;

public class GenLayer extends ByteMap2D
{
    public final long baseSeed;
    private final ArrayList<GenLayer> dependencies;

    private int lastOffsetX;
    private int lastOffsetZ;

    public GenLayer(int lengthX, int lengthZ, long baseSeed) {
        super(lengthX, lengthZ);

        this.baseSeed = baseSeed;
        dependencies = new ArrayList<>();
    }

    public Iterable<GenLayer> iterateDependencies() {
        return dependencies;
    }

    protected void addDependency(GenLayer dependency) {
        dependencies.add(dependency);
    }

    protected Random rngFromPosition(Random random, int x, int z) {
        long columnSeed = IntNoise.white2D(x, z, baseSeed);

        if (random == null)
            random = new Random(columnSeed);
        else
            random.setSeed(columnSeed);

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