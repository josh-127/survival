package net.survival.world.chunk;

public class ChunkPos
{
    private ChunkPos() {}

    public static boolean positionEquals(int cx1, int cz1, int cx2, int cz2) {
        return cx1 == cx2 && cz1 == cz2;
    }

    public static int toChunkX(int x) {
        return x / Chunk.XLENGTH;
    }

    public static int toChunkZ(int z) {
        return z / Chunk.ZLENGTH;
    }

    public static int toLocalX(int cx, int gx) {
        return gx - cx * Chunk.XLENGTH;
    }

    public static int toLocalZ(int cz, int gz) {
        return gz - cz * Chunk.ZLENGTH;
    }

    public static int toGlobalX(int cx, int lx) {
        return lx + cx * Chunk.XLENGTH;
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
    
    /**
     * Hashes a chunk position into a 64-bit integer.
     * Bits 0-11: reserved
     * Bits 12-32: chunk x-position
     * Bits 33-53: chunk z-position
     * Bits 53-63: reserved
     * Out-of-bound chunk positions cause undefined behavior.
     * @param cx chunk x-position
     * @param cz chunk z-position
     * @return the hashed position
     */
    public static long hashPos(int cx, int cz) {
        return (((cz + 1048576) & 0x1FFFFFL) << 33L) |
                (((cx + 1048576) & 0x1FFFFFL) << 12L);
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
     * Retrieves the chunk z-position from a hashed position.
     * @param hashedPos hashed position
     * @return chunk z-position
     */
    public static int chunkZFromHashedPos(long hashedPos) {
        return (int) ((hashedPos & 0x3FFFFE00000000L) >>> 33L) - 1048576;
    }
}