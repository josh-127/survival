package net.survival.gen;

import net.survival.block.Column;
import net.survival.block.state.AirBlock;
import net.survival.block.state.BlockState;

public class ColumnPrimer {
    public static final int XLENGTH = Column.XLENGTH;
    public static final int YLENGTH = 256;
    public static final int ZLENGTH = Column.ZLENGTH;
    public static final int BASE_AREA = XLENGTH * ZLENGTH;
    public static final int VOLUME = BASE_AREA * YLENGTH;

    public final BlockState[] blocks;

    public ColumnPrimer() {
        blocks = new BlockState[VOLUME];

        for (int i = 0; i < blocks.length; ++i) {
            blocks[i] = AirBlock.INSTANCE;
        }
    }

    public Column toColumn() {
        var column = new Column();
        var index = 0;

        for (var y = 0; y < YLENGTH; ++y) {
            for (var z = 0; z < ZLENGTH; ++z) {
                for (var x = 0; x < XLENGTH; ++x) {
                    if (!(blocks[index] instanceof AirBlock)) {
                        column.setBlock(x, y, z, blocks[index]);
                    }
                    ++index;
                }
            }
        }

        return column;
    }

    public BlockState getBlock(int x, int y, int z) {
        return blocks[localPositionToIndex(x, y, z)];
    }

    public BlockState sampleNearestBlock(double x, double y, double z) {
        var xi = (int) Math.floor(x);
        var yi = (int) Math.floor(y);
        var zi = (int) Math.floor(z);

        return getBlock(xi, yi, zi);
    }

    public int getTopLevel(int x, int z) {
        var topLevel = YLENGTH - 1;
        while (topLevel >= 0 && getBlock(x, topLevel, z) instanceof AirBlock) {
            --topLevel;
        }

        return topLevel;
    }

    public void setBlock(int x, int y, int z, BlockState to) {
        if (to == null) {
            throw new IllegalArgumentException("to");
        }

        blocks[localPositionToIndex(x, y, z)] = to;
    }

    public boolean placeBlockIfEmpty(int x, int y, int z, BlockState to) {
        if (getBlock(x, y, z) instanceof AirBlock) {
            setBlock(x, y, z, to);
            return true;
        }

        return false;
    }

    public boolean replaceBlockIfExists(int x, int y, int z, BlockState replacement) {
        if (!(getBlock(x, y, z) instanceof AirBlock)) {
            setBlock(x, y, z, replacement);
            return true;
        }

        return false;
    }

    public void clear() {
        for (var i = 0; i < blocks.length; ++i) {
            blocks[i] = AirBlock.INSTANCE;
        }
    }

    private int localPositionToIndex(int x, int y, int z) {
        return x + (z * XLENGTH) + (y * BASE_AREA);
    }
}