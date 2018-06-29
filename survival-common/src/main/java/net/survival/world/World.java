package net.survival.world;

import java.util.HashMap;
import java.util.Map;

import net.survival.entity.Entity;
import net.survival.world.chunk.Chunk;

public class World
{
    public static final double TICKS_PER_SECOND = 30.0;
    public static final double SECONDS_PER_TICK = 1.0 / TICKS_PER_SECOND;
    
    private final HashMap<Long, Chunk> chunks;
    
    public World() {
        chunks = new HashMap<>();
    }
    
    public Chunk getChunk(int cx, int cy, int cz) {
        return chunks.get(Chunk.hashPos(cx, cy, cz));
    }
    
    public Chunk getChunk(long hashedPos) {
        return chunks.get(hashedPos);
    }
    
    public Iterable<Map.Entry<Long, Chunk>> iterateChunkMap() {
        return chunks.entrySet();
    }
    
    public Iterable<Chunk> iterateChunks() {
        return chunks.values();
    }
    
    public void addChunk(Chunk chunk) {
        chunks.put(Chunk.hashPos(chunk), chunk);
    }
    
    public void removeChunk(int cx, int cy, int cz) {
        chunks.remove(Chunk.hashPos(cx, cy, cz));
    }
    
    public void removeChunk(long hashedPos) {
        chunks.remove(hashedPos);
    }

    public short getBlockID(int x, int y, int z) {
        int cx = Chunk.toChunkX(x);
        int cy = Chunk.toChunkY(y);
        int cz = Chunk.toChunkZ(z);

        Chunk chunk = chunks.get(Chunk.hashPos(cx, cy, cz));
        if (chunk == null)
            throw new RuntimeException("Cannot query a block in an unloaded chunk.");

        int localX = chunk.toLocalX(x);
        int localY = chunk.toLocalY(y);
        int localZ = chunk.toLocalZ(z);

        return chunk.getBlockID(localX, localY, localZ);
    }

    public void setBlockID(int x, int y, int z, short to) {
        int cx = Chunk.toChunkX(x);
        int cy = Chunk.toChunkY(y);
        int cz = Chunk.toChunkZ(z);

        Chunk chunk = chunks.get(Chunk.hashPos(cx, cy, cz));
        if (chunk == null)
            throw new RuntimeException("Cannot place/replace a block in an unloaded chunk.");

        int localX = chunk.toLocalX(x);
        int localY = chunk.toLocalY(y);
        int localZ = chunk.toLocalZ(z);
        
        chunk.setBlockID(localX, localY, localZ, to);
    }
    
    public void addEntity(Entity entity) {
        int cx = Chunk.toChunkX((int) entity.getX());
        int cy = Chunk.toChunkY((int) entity.getY());
        int cz = Chunk.toChunkZ((int) entity.getZ());

        Chunk chunk = chunks.get(Chunk.hashPos(cx, cy, cz));
        if (chunk == null)
            throw new RuntimeException("Cannot place an entity in an unloaded chunk.");
        
        chunk.addEntity(entity);
    }
}