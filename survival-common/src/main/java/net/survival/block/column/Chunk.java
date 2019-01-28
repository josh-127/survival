package net.survival.block.column;

import java.util.Arrays;

import net.survival.block.BlockStorage;

public class Chunk implements BlockStorage
{
    public static final int XLENGTH = 16;
    public static final int YLENGTH = 16;
    public static final int ZLENGTH = 16;
    public static final int BASE_AREA = XLENGTH * ZLENGTH;
    public static final int VOLUME = BASE_AREA * YLENGTH;

    public final int[] blockIDs;

    public Chunk() {
        blockIDs = new int[VOLUME];
    }

    private Chunk(int[] blockIDs) {
        this.blockIDs = blockIDs;
    }

    public Chunk makeCopy() {
        var copyOfBlockIDs = Arrays.copyOf(blockIDs, VOLUME);
        return new Chunk(copyOfBlockIDs);
    }

    @Override
    public int getBlockFullID(int x, int y, int z) {
        return blockIDs[localPositionToIndex(x, y, z)];
    }

    @Override
    public void setBlockFullID(int x, int y, int z, int to) {
        blockIDs[localPositionToIndex(x, y, z)] = to;
    }

    public int localPositionToIndex(int x, int y, int z) {
        return x + (z * XLENGTH) + (y * BASE_AREA);
    }

    public boolean isInBounds(int x, int y, int z) {
        return x >= 0 && y >= 0 && z >= 0 && x < XLENGTH && y < YLENGTH && z < ZLENGTH;
    }
}