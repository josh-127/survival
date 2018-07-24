package net.survival.world.chunk;

import java.util.ArrayList;

import net.survival.entity.Entity;

public class Chunk
{
    public static final int XLENGTH = 16;
    public static final int YLENGTH = 256;
    public static final int ZLENGTH = 16;
    public static final int BASE_AREA = XLENGTH * ZLENGTH;
    public static final int VOLUME = BASE_AREA * YLENGTH;

    public static final int BLOCKS_MODIFIED = 1;
    public static final int ENTITIES_MODIFIED = 2;

    public final short[] blockIDs;
    private final ArrayList<Entity> entities;

    private int modified;
    private boolean decorated;

    public Chunk() {
        blockIDs = new short[VOLUME];
        entities = new ArrayList<>();
    }

    public short getBlockID(int lx, int ly, int lz) {
        return blockIDs[localPositionToIndex(lx, ly, lz)];
    }

    public void setBlockID(int lx, int ly, int lz, short to) {
        blockIDs[localPositionToIndex(lx, ly, lz)] = to;
        modified |= BLOCKS_MODIFIED;
    }

    public int localPositionToIndex(int x, int y, int z) {
        return x + (z * XLENGTH) + (y * BASE_AREA);
    }

    public boolean isInBounds(int lx, int ly, int lz) {
        return lx >= 0 && ly >= 0 && lz >= 0 && lx < XLENGTH && ly < YLENGTH && lz < ZLENGTH;
    }

    public int getTopBlockY(int lx, int lz) {
        int topLevel = YLENGTH - 1;

        while (topLevel >= 0 && getBlockID(lx, topLevel, lz) == 0)
            --topLevel;

        return topLevel;
    }
    
    public void placeBlockIdIfEmpty(int lx, int ly, int lz, short blockID) {
        int index = localPositionToIndex(lx, ly, lz);
        
        if (blockIDs[index] != 0)
            blockIDs[index] = blockID;
    }

    public Iterable<Entity> iterateEntities() {
        modified |= ENTITIES_MODIFIED;
        return entities;
    }

    public void addEntity(Entity entity) {
        entities.add(entity);
        modified |= ENTITIES_MODIFIED;
    }

    public int getModificationFlags() {
        return modified;
    }

    public boolean isBlocksModified() {
        return (modified & BLOCKS_MODIFIED) != 0;
    }

    public boolean isEntitiesModified() {
        return (modified & ENTITIES_MODIFIED) != 0;
    }

    public void clearModificationFlags() {
        modified = 0;
    }

    public void setModificationFlags(int to) {
        modified = to;
    }

    public boolean isDecorated() {
        return decorated;
    }

    public void markDecorated() {
        decorated = true;
    }
}