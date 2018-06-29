package net.survival.world.chunk;

public class ChunkPos
{
    private ChunkPos() {}

    public static boolean positionEquals(int cx1, int cy1, int cz1, int cx2, int cy2, int cz2) {
        return cx1 == cx2 && cy1 == cy2 && cz1 == cz2;
    }

    public static int toChunkX(int x) {
        return x / Chunk.XLENGTH;
    }
    
    public static int toChunkY(int y) {
        return y / Chunk.YLENGTH;
    }

    public static int toChunkZ(int z) {
        return z / Chunk.ZLENGTH;
    }

    public static int toLocalX(int cx, int gx) {
        return gx - cx * Chunk.XLENGTH;
    }
    
    public static int toLocalY(int cy, int gy) {
        return gy - cy * Chunk.YLENGTH;
    }

    public static int toLocalZ(int cz, int gz) {
        return gz - cz * Chunk.ZLENGTH;
    }

    public static int toGlobalX(int cx, int lx) {
        return lx + cx * Chunk.XLENGTH;
    }
    
    public static int toGlobalY(int cy, int ly) {
        return ly + cy * Chunk.YLENGTH;
    }

    public static int toGlobalZ(int cz, int lz) {
        return lz + cz * Chunk.ZLENGTH;
    }
    
    public static int getGlobalWestBound(int cx) {
        return toGlobalX(cx, 0);
    }
    
    public static int getGlobalEastBound(int cx) {
        return toGlobalX(cx, Chunk.XLENGTH);
    }
    
    public static int getGlobalNorthBound(int cz) {
        return toGlobalZ(cz, 0);
    }
    
    public static int getGlobalSouthBound(int cz) {
        return toGlobalZ(cz, Chunk.ZLENGTH);
    }
    
    public static int getGlobalBottomBound(int cy) {
        return toGlobalY(cy, 0);
    }
    
    public static int getGlobalTopBound(int cy) {
        return toGlobalY(cy, Chunk.YLENGTH);
    }
    
    /**
     * Hashes a chunk position into a 64-bit integer.
     * Bits 0-11: chunk y-position
     * Bits 12-32: chunk x-position
     * Bits 33-53: chunk z-position
     * Bits 53-63: reserved
     * Out-of-bound chunk positions cause undefined behavior.
     * @param cx chunk x-position
     * @param cy chunk y-position
     * @param cz chunk z-position
     * @return the hashed position
     */
    public static long hashPos(int cx, int cy, int cz) {
        return (((cz + 1048576) & 0x1FFFFFL) << 33L) |
                (((cx + 1048576) & 0x1FFFFFL) << 12L) |
                ((cy + 2048) & 0xFFFL);
    }
    
    /**
     * Retrieves the chunk x-position from a hashed position.
     * @param hashedPos hashed position
     * @return chunk x-position
     */
    public static int chunkXFromHashedPos(long hashedPos) {
        return (int) ((hashedPos & 0x1FFFFF000L) >>> 12L) - 1048576;
    }
    
    /**
     * Retrieves the chunk y-position from a hashed position.
     * @param hashedPos hashed position
     * @return chunk y-position
     */
    public static int chunkYFromHashedPos(long hashedPos) {
        return (int) (hashedPos & 0xFFFL) - 2048;
    }
    
    /**
     * Retrieves the chunk z-position from a hashed position.
     * @param hashedPos hashed position
     * @return chunk z-position
     */
    public static int chunkZFromHashedPos(long hashedPos) {
        return (int) ((hashedPos & 0x3FFFFE00000000L) >>> 33L) - 1048576;
    }
}