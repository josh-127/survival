package net.survival.world;

import net.survival.block.BlockType;
import net.survival.world.column.Column;

public interface BlockStorage
{
    short getBlock(int x, int y, int z);

    void setBlock(int x, int y, int z, short to);

    default BlockType getBlockType(int x, int y, int z) {
        return BlockType.byID(getBlock(x, y, z));
    }

    default short sampleNearestBlock(double x, double y, double z) {
        return getBlock((int) Math.floor(x), (int) Math.floor(y), (int) Math.floor(z));
    }

    default BlockType sampleNearestBlockType(double x, double y, double z) {
        return getBlockType((int) Math.floor(x), (int) Math.floor(y), (int) Math.floor(z));
    }

    default int getTopLevel(int x, int z) {
        int topLevel = Column.YLENGTH - 1;

        while (topLevel >= 0 && getBlock(x, topLevel, z) == 0)
            --topLevel;

        return topLevel;
    }

    default boolean placeBlockIfEmpty(int x, int y, int z, short blockID) {
        if (getBlock(x, y, z) == 0) {
            setBlock(x, y, z, blockID);
            return true;
        }

        return false;
    }

    default boolean replaceBlockIfExists(int x, int y, int z, short replacement) {
        if (getBlock(x, y, z) != 0) {
            setBlock(x, y, z, replacement);
            return true;
        }

        return false;
    }
}