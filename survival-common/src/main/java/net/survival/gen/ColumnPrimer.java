package net.survival.gen;

import net.survival.block.Column;
import net.survival.blocktype.Block;
import net.survival.blocktype.BlockType;

class ColumnPrimer
{
    public static final int XLENGTH = Column.XLENGTH;
    public static final int YLENGTH = 256;
    public static final int ZLENGTH = Column.ZLENGTH;
    public static final int BASE_AREA = XLENGTH * ZLENGTH;
    public static final int VOLUME = BASE_AREA * YLENGTH;

    public final int[] blockIDs = new int[VOLUME];

    public Column toColumn() {
        var column = new Column();
        var index = 0;

        for (var y = 0; y < YLENGTH; ++y) {
            for (var z = 0; z < ZLENGTH; ++z) {
                for (var x = 0; x < XLENGTH; ++x) {
                    column.setBlockFullID(x, y, z, blockIDs[index]);
                    ++index;
                }
            }
        }

        return column;
    }

    public int getBlockFullID(int x, int y, int z) {
        return blockIDs[localPositionToIndex(x, y, z)];
    }

    public Block getBlock(int x, int y, int z) {
        return BlockType.byFullID(getBlockFullID(x, y, z));
    }

    public int sampleNearestBlockFullID(double x, double y, double z) {
        return getBlockFullID((int) Math.floor(x), (int) Math.floor(y), (int) Math.floor(z));
    }

    public Block sampleNearestBlock(double x, double y, double z) {
        return BlockType.byFullID(sampleNearestBlockFullID(x, y, z));
    }

    public int getTopLevel(int x, int z) {
        var topLevel = YLENGTH - 1;
        while (topLevel >= 0 && getBlockFullID(x, topLevel, z) == 0)
            --topLevel;

        return topLevel;
    }

    public void setBlockFullID(int x, int y, int z, int to) {
        blockIDs[localPositionToIndex(x, y, z)] = to;
    }

    public void setBlock(int x, int y, int z, Block to) {
        setBlockFullID(x, y, z, to.getFullID());
    }

    public boolean placeBlockIfEmpty(int x, int y, int z, int to) {
        if (getBlockFullID(x, y, z) == 0) {
            setBlockFullID(x, y, z, to);
            return true;
        }

        return false;
    }

    public boolean placeBlockIfEmpty(int x, int y, int z, Block to) {
        return placeBlockIfEmpty(x, y, z, to.getFullID());
    }

    public boolean replaceBlockIfExists(int x, int y, int z, int replacement) {
        if (getBlockFullID(x, y, z) != 0) {
            setBlockFullID(x, y, z, replacement);
            return true;
        }

        return false;
    }

    public boolean replaceBlockIfExists(int x, int y, int z, Block replacement) {
        return replaceBlockIfExists(x, y, z, replacement.getFullID());
    }

    public void clear() {
        for (var i = 0; i < blockIDs.length; ++i)
            blockIDs[i] = 0;
    }

    private int localPositionToIndex(int x, int y, int z) {
        return x + (z * XLENGTH) + (y * BASE_AREA);
    }
}