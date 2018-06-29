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
    private final ArrayList<Entity> entities;
    
    public Chunk() {
        blockIDs = new short[VOLUME];
        entities = new ArrayList<>();
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
}