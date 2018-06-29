package net.survival.world.chunk;

import java.util.ArrayList;

import net.survival.entity.Entity;

public class Chunk
{
    public static final int XLENGTH = 32;
    public static final int YLENGTH = 32;
    public static final int ZLENGTH = 32;
    public static final int BASE_AREA = XLENGTH * ZLENGTH;
    public static final int VOLUME = BASE_AREA * YLENGTH;
    
    private final short[] blockIDs;
    public final int chunkX;
    public final int chunkY;
    public final int chunkZ;
    
    private final ArrayList<Entity> entities;
    
    public Chunk(int cx, int cy, int cz) {
        blockIDs = new short[VOLUME];
        chunkX = cx;
        chunkY = cy;
        chunkZ = cz;
        
        entities = new ArrayList<>();
    }
    
    public Chunk(long hashedPos) {
        this(chunkXFromHashedPos(hashedPos), chunkYFromHashedPos(hashedPos), chunkZFromHashedPos(hashedPos));
    }
    
    public short getBlockID(int localX, int localY, int localZ) {
        return blockIDs[localXyzToIndex(localX, localY, localZ)];
    }
    
    public void setBlockID(int localX, int localY, int localZ, short to) {
        blockIDs[localXyzToIndex(localX, localY, localZ)] = to;
    }

    private int localXyzToIndex(int x, int y, int z) {
        return x + (z * XLENGTH) + (y * BASE_AREA);
    }
    
    public Iterable<Entity> iterateEntities() {
        return entities;
    }
    
    public void addEntity(Entity entity) {
        entities.add(entity);
    }
    
    public boolean load(String filePath) {
        return false;
    }
    
    public void save(String filePath) {}
    
    public boolean isInBounds(int lx, int ly, int lz) {
        return lx >= 0 && ly >= 0 && lz >= 0 && lx < XLENGTH && ly < YLENGTH && lz < ZLENGTH;
    }

    public boolean positionEquals(Chunk other) {
        return chunkX == other.chunkX && chunkY == other.chunkY && chunkZ == other.chunkZ;
    }

    public boolean positionEquals(int cx, int cy, int cz) {
        return chunkX == cx && chunkY == cy && chunkZ == cz;
    }

    public static int toChunkX(int x) {
        return x / XLENGTH;
    }
    
    public static int toChunkY(int y) {
        return y / YLENGTH;
    }

    public static int toChunkZ(int z) {
        return z / ZLENGTH;
    }

    public int toLocalX(int gx) {
        return gx - chunkX * XLENGTH;
    }
    
    public int toLocalY(int gy) {
        return gy - chunkY * YLENGTH;
    }

    public int toLocalZ(int gz) {
        return gz - chunkZ * ZLENGTH;
    }

    public int toGlobalX(int lx) {
        return lx + chunkX * XLENGTH;
    }
    
    public int toGlobalY(int ly) {
        return ly + chunkY * YLENGTH;
    }

    public int toGlobalZ(int lz) {
        return lz + chunkZ * ZLENGTH;
    }
    
    public int getGlobalWestBound() {
        return toGlobalX(0);
    }
    
    public int getGlobalEastBound() {
        return toGlobalX(XLENGTH);
    }
    
    public int getGlobalNorthBound() {
        return toGlobalZ(0);
    }
    
    public int getGlobalSouthBound() {
        return toGlobalZ(ZLENGTH);
    }
    
    public int getGlobalBottomBound() {
        return toGlobalY(0);
    }
    
    public int getGlobalTopBound() {
        return toGlobalY(YLENGTH);
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
     * Hashes a chunk position into a 64-bit integer.
     * @param chunk chunk position
     * @return the hashed position
     */
    public static long hashPos(Chunk chunk) {
        return hashPos(chunk.chunkX, chunk.chunkY, chunk.chunkZ);
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
    
    @Override
    public int hashCode() {
        long hash = 2166136261L;
        hash ^= chunkX;
        hash *= 16777619L;
        hash ^= chunkY;
        hash *= 16777619L;
        hash ^= chunkZ;
        hash *= 16777619L;
        return (int) hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Chunk) {
            Chunk other = (Chunk) obj;
            return positionEquals(other);
        }

        return false;
    }

    @Override
    public String toString() {
        return String.format("Chunk(%d, %d, %d)", chunkX, chunkY, chunkZ);
    }
}