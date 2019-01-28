package net.survival.block;

import net.survival.block.column.Column;
import net.survival.blocktype.Block;
import net.survival.blocktype.BlockType;

public interface BlockStorage
{
    int getBlockFullID(int x, int y, int z);

    void setBlockFullID(int x, int y, int z, int to);

    default Block getBlockState(int x, int y, int z) {
        return BlockType.byFullID(getBlockFullID(x, y, z));
    }

    default void setBlockState(int x, int y, int z, Block to) {
        setBlockFullID(x, y, z, to.getFullID());
    }

    default int sampleNearestBlock(double x, double y, double z) {
        return getBlockFullID((int) Math.floor(x), (int) Math.floor(y), (int) Math.floor(z));
    }

    default Block sampleNearestBlockState(double x, double y, double z) {
        return getBlockState((int) Math.floor(x), (int) Math.floor(y), (int) Math.floor(z));
    }

    default int getTopLevel(int x, int z) {
        var topLevel = Column.YLENGTH - 1;

        while (topLevel >= 0 && getBlockFullID(x, topLevel, z) == 0)
            --topLevel;

        return topLevel;
    }

    default boolean placeBlockIfEmpty(int x, int y, int z, short blockID) {
        if (getBlockFullID(x, y, z) == 0) {
            setBlockFullID(x, y, z, blockID);
            return true;
        }

        return false;
    }

    default boolean replaceBlockIfExists(int x, int y, int z, short replacement) {
        if (getBlockFullID(x, y, z) != 0) {
            setBlockFullID(x, y, z, replacement);
            return true;
        }

        return false;
    }
}