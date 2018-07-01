package net.survival.world.gen.v1.terrain;

import java.util.ArrayList;

import net.survival.world.chunk.ChunkPos;

public class CachedTerrainGenerator
{
    private static final int CACHE_LIMIT = 32;
    
    private final TerrainGenerator underlyingGenerator;
    private final ArrayList<ChunkPrimer> cache;
    
    public CachedTerrainGenerator(long seed) {
        underlyingGenerator = new TerrainGenerator(seed);
        cache = new ArrayList<>(CACHE_LIMIT);
    }
    
    public ChunkPrimer generate(int cx, int cy, int cz) {
        for (ChunkPrimer cp : cache) {
            if (ChunkPos.positionEquals(cp.chunkX, cp.chunkY, cp.chunkZ, cx, cy, cz))
                return cp;
        }

        ChunkPrimer chunkPrimer = underlyingGenerator.generate(cx, cy, cz);
        
        if (cache.size() >= CACHE_LIMIT)
            cache.remove(0);
        
        cache.add(chunkPrimer);
        return chunkPrimer;
    }
}