package net.survival.world.column;

import java.util.Arrays;

import net.survival.world.BlockStorage;

public class Chunk implements BlockStorage
{
    public static final int XLENGTH = 16;
    public static final int YLENGTH = 16;
    public static final int ZLENGTH = 16;
    public static final int BASE_AREA = XLENGTH * ZLENGTH;
    public static final int VOLUME = BASE_AREA * YLENGTH;

    public final short[] blockIDs;

    public Chunk() {
        blockIDs = new short[VOLUME];
    }

    private Chunk(short[] blockIDs) {
        this.blockIDs = blockIDs;
    }

    public Chunk makeCopy() {
        short[] copyOfBlockIDs = Arrays.copyOf(blockIDs, VOLUME);
        return new Chunk(copyOfBlockIDs);
    }

    @Override
    public short getBlock(int x, int y, int z) {
        return blockIDs[localPositionToIndex(x, y, z)];
    }

    @Override
    public void setBlock(int x, int y, int z, short to) {
        blockIDs[localPositionToIndex(x, y, z)] = to;
    }

    public int localPositionToIndex(int x, int y, int z) {
        return x + (z * XLENGTH) + (y * BASE_AREA);
    }

    public boolean isInBounds(int x, int y, int z) {
        return x >= 0 && y >= 0 && z >= 0 && x < XLENGTH && y < YLENGTH && z < ZLENGTH;
    }
}