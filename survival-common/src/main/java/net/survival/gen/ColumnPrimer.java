package net.survival.gen;

import net.survival.block.Column;
import net.survival.blocktype.Block;
import net.survival.blocktype.BlockType;

public class ColumnPrimer
{
    public static final int XLENGTH = Column.XLENGTH;
    public static final int YLENGTH = 256;
    public static final int ZLENGTH = Column.ZLENGTH;
    public static final int BASE_AREA = XLENGTH * ZLENGTH;
    public static final int VOLUME = BASE_AREA * YLENGTH;

    public final int[] blockIds = new int[VOLUME];

    public Column toColumn() {
        var column = new Column();
        var index = 0;

        for (var y = 0; y < YLENGTH; ++y) {
            for (var z = 0; z < ZLENGTH; ++z) {
                for (var x = 0; x < XLENGTH; ++x) {
                    if (blockIds[index] != 0)
                        column.setBlockFullId(x, y, z, blockIds[index]);
                    ++index;
                }
            }
        }

        return column;
    }

    public int getBlockFullId(int x, int y, int z) {
        return blockIds[localPositionToIndex(x, y, z)];
    }

    public Block getBlock(int x, int y, int z) {
        return BlockType.byFullId(getBlockFullId(x, y, z));
    }

    public int sampleNearestBlockFullId(double x, double y, double z) {
        return getBlockFullId((int) Math.floor(x), (int) Math.floor(y), (int) Math.floor(z));
    }

    public Block sampleNearestBlock(double x, double y, double z) {
        return BlockType.byFullId(sampleNearestBlockFullId(x, y, z));
    }

    public int getTopLevel(int x, int z) {
        var topLevel = YLENGTH - 1;
        while (topLevel >= 0 && getBlockFullId(x, topLevel, z) == 0)
            --topLevel;

        return topLevel;
    }

    public void setBlockFullId(int x, int y, int z, int to) {
        blockIds[localPositionToIndex(x, y, z)] = to;
    }

    public void setBlock(int x, int y, int z, Block to) {
        setBlockFullId(x, y, z, to.getFullId());
    }

    public boolean placeBlockIfEmpty(int x, int y, int z, int to) {
        if (getBlockFullId(x, y, z) == 0) {
            setBlockFullId(x, y, z, to);
            return true;
        }

        return false;
    }

    public boolean placeBlockIfEmpty(int x, int y, int z, Block to) {
        return placeBlockIfEmpty(x, y, z, to.getFullId());
    }

    public boolean replaceBlockIfExists(int x, int y, int z, int replacement) {
        if (getBlockFullId(x, y, z) != 0) {
            setBlockFullId(x, y, z, replacement);
            return true;
        }

        return false;
    }

    public boolean replaceBlockIfExists(int x, int y, int z, Block replacement) {
        return replaceBlockIfExists(x, y, z, replacement.getFullId());
    }

    public void clear() {
        for (var i = 0; i < blockIds.length; ++i)
            blockIds[i] = 0;
    }

    private int localPositionToIndex(int x, int y, int z) {
        return x + (z * XLENGTH) + (y * BASE_AREA);
    }
}