package net.survival.block;

public class ColumnPos {
    private ColumnPos() {}

    public static boolean positionEquals(int cx1, int cz1, int cx2, int cz2) {
        return cx1 == cx2 && cz1 == cz2;
    }

    public static int toColumnX(int x) {
        return (int) Math.floor((double) x / Column.XLENGTH);
    }

    public static int toColumnZ(int z) {
        return (int) Math.floor((double) z / Column.ZLENGTH);
    }

    public static int toLocalX(int cx, int gx) {
        return gx - cx * Column.XLENGTH;
    }

    public static int toLocalZ(int cz, int gz) {
        return gz - cz * Column.ZLENGTH;
    }

    public static int toGlobalX(int cx, int lx) {
        return lx + cx * Column.XLENGTH;
    }

    public static int toGlobalZ(int cz, int lz) {
        return lz + cz * Column.ZLENGTH;
    }

    public static int getGlobalWestBound(int cx) {
        return toGlobalX(cx, 0);
    }

    public static int getGlobalEastBound(int cx) {
        return toGlobalX(cx, Column.XLENGTH);
    }

    public static int getGlobalNorthBound(int cz) {
        return toGlobalZ(cz, 0);
    }

    public static int getGlobalSouthBound(int cz) {
        return toGlobalZ(cz, Column.ZLENGTH);
    }

    public static boolean isInBounds(int lx, int ly, int lz) {
        return
                lx >= 0 &&
                lx < Column.XLENGTH &&
                ly >= 0 &&
                lz >= 0 &&
                lz < Column.ZLENGTH;
    }

    /**
     * Hashes a column position into a 64-bit integer. Bits 0-31: column x-position
     * Bits 32-63: column z-position
     * 
     * @param cx
     *            column x-position
     * @param cz
     *            column z-position
     * @return the hashed position
     */
    public static long hashPos(int cx, int cz) {
        return ((cz & 0xFFFFFFFFL) << 32L) | (cx & 0xFFFFFFFFL);
    }

    /**
     * Retrieves the column x-position from a hashed position.
     * 
     * @param hashedPos
     *            hashed position
     * @return column x-position
     */
    public static int columnXFromHashedPos(long hashedPos) {
        return (int) (hashedPos & 0x00000000FFFFFFFFL);
    }

    /**
     * Retrieves the column z-position from a hashed position.
     * 
     * @param hashedPos
     *            hashed position
     * @return column z-position
     */
    public static int columnZFromHashedPos(long hashedPos) {
        return (int) ((hashedPos & 0xFFFFFFFF00000000L) >>> 32L);
    }
}