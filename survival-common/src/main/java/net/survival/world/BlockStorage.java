package net.survival.world;

import net.survival.world.chunk.Chunk;

public interface BlockStorage
{
    short getBlockID(int x, int y, int z);

    void setBlockID(int x, int y, int z, short to);

    default int getTopBlockY(int x, int z) {
        int topLevel = Chunk.YLENGTH - 1;

        while (topLevel >= 0 && getBlockID(x, topLevel, z) == 0)
            --topLevel;

        return topLevel;
    }

    default void placeBlockIdIfEmpty(int x, int y, int z, short blockID) {
        if (getBlockID(x, y, z) == 0)
            setBlockID(x, y, z, blockID);
    }
}