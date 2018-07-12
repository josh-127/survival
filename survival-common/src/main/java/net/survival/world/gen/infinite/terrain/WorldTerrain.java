package net.survival.world.gen.infinite.terrain;

import java.util.ArrayList;

import net.survival.world.chunk.ChunkPos;

public class WorldTerrain
{
    private static final int CACHE_LIMIT = 32;
    
    private final ArrayList<ChunkPrimer> cache;
    private final TerrainGenerator terrainGenerator;

    public WorldTerrain(long seed) {
        cache = new ArrayList<>(CACHE_LIMIT);
        terrainGenerator = new TerrainGenerator(seed);
    }
    
    public ChunkPrimer getChunkPrimer(int cx, int cy, int cz) {
        for (ChunkPrimer cp : cache) {
            if (ChunkPos.positionEquals(cp.chunkX, cp.chunkY, cp.chunkZ, cx, cy, cz))
                return cp;
        }

        ChunkPrimer chunkPrimer = terrainGenerator.generate(cx, cy, cz);
        
        if (cache.size() >= CACHE_LIMIT)
            cache.remove(cache.size() - 1);
        
        cache.add(0, chunkPrimer);
        return chunkPrimer;
    }
    
    public short getBlockID(int x, int y, int z) {
        int cx = ChunkPos.toChunkX(x);
        int cy = ChunkPos.toChunkY(y);
        int cz = ChunkPos.toChunkZ(z);

        ChunkPrimer chunkPrimer = getChunkPrimer(cx, cy, cz);
        
        int localX = ChunkPos.toLocalX(cx, x);
        int localY = ChunkPos.toLocalY(cy, y);
        int localZ = ChunkPos.toLocalZ(cz, z);

        return chunkPrimer.getBlockID(localX, localY, localZ);
    }
}