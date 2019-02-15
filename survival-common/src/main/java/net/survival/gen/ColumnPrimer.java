package net.survival.gen;

import net.survival.block.Column;

class ColumnPrimer
{
    public static final int XLENGTH = Column.XLENGTH;
    public static final int YLENGTH = 128;
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

    public void setBlockFullID(int x, int y, int z, int to) {
        blockIDs[localPositionToIndex(x, y, z)] = to;
    }

    public void clear() {
        for (var i = 0; i < blockIDs.length; ++i)
            blockIDs[i] = 0;
    }

    private int localPositionToIndex(int x, int y, int z) {
        return x + (z * XLENGTH) + (y * BASE_AREA);
    }
}