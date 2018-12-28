package net.survival.world.chunk;

public class ChunkColumnPos
{
    private ChunkColumnPos() {}

    public static boolean positionEquals(int cx1, int cz1, int cx2, int cz2) {
        return cx1 == cx2 && cz1 == cz2;
    }

    public static int toChunkX(int x) {
        return (int) Math.floor((double) x / ChunkColumn.XLENGTH);
    }

    public static int toChunkZ(int z) {
        return (int) Math.floor((double) z / ChunkColumn.ZLENGTH);
    }

    public static int toLocalX(int cx, int gx) {
        return gx - cx * ChunkColumn.XLENGTH;
    }

    public static int toLocalZ(int cz, int gz) {
        return gz - cz * ChunkColumn.ZLENGTH;
    }

    public static int toGlobalX(int cx, int lx) {
        return lx + cx * ChunkColumn.XLENGTH;
    }

    public static int toGlobalZ(int cz, int lz) {
        return lz + cz * ChunkColumn.ZLENGTH;
    }

    public static int getGlobalWestBound(int cx) {
        return toGlobalX(cx, 0);
    }

    public static int getGlobalEastBound(int cx) {
        return toGlobalX(cx, ChunkColumn.XLENGTH);
    }

    public static int getGlobalNorthBound(int cz) {
        return toGlobalZ(cz, 0);
    }

    public static int getGlobalSouthBound(int cz) {
        return toGlobalZ(cz, ChunkColumn.ZLENGTH);
    }

    public static boolean isInBounds(int lx, int ly, int lz) {
        return lx >= 0 && lx < ChunkColumn.XLENGTH && ly >= 0 && ly < ChunkColumn.YLENGTH && lz >= 0
                && lz < ChunkColumn.ZLENGTH;
    }

    /**
     * Hashes a chunk position into a 64-bit integer. Bits 0-31: chunk x-position
     * Bits 32-63: chunk z-position
     * 
     * @param cx
     *            chunk x-position
     * @param cz
     *            chunk z-position
     * @return the hashed position
     */
    public static long hashPos(int cx, int cz) {
        return ((cz & 0xFFFFFFFFL) << 32L) | (cx & 0xFFFFFFFFL);
    }

    /**
     * Retrieves the chunk x-position from a hashed position.
     * 
     * @param hashedPos
     *            hashed position
     * @return chunk x-position
     */
    public static int chunkXFromHashedPos(long hashedPos) {
        return (int) (hashedPos & 0x00000000FFFFFFFFL);
    }

    /**
     * Retrieves the chunk z-position from a hashed position.
     * 
     * @param hashedPos
     *            hashed position
     * @return chunk z-position
     */
    public static int chunkZFromHashedPos(long hashedPos) {
        return (int) ((hashedPos & 0xFFFFFFFF00000000L) >>> 32L);
    }
}