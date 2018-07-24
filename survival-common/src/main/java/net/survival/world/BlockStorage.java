package net.survival.world;

import net.survival.world.chunk.Chunk;

public interface BlockStorage
{
    short getBlock(int x, int y, int z);

    void setBlock(int x, int y, int z, short to);

    default int getTopLevel(int x, int z) {
        int topLevel = Chunk.YLENGTH - 1;

        while (topLevel >= 0 && getBlock(x, topLevel, z) == 0)
            --topLevel;

        return topLevel;
    }

    default void placeBlockIfEmpty(int x, int y, int z, short blockID) {
        if (getBlock(x, y, z) == 0)
            setBlock(x, y, z, blockID);
    }
}