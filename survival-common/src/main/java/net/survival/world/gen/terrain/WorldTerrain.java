package net.survival.world.gen.terrain;

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
    
    public ChunkPrimer getChunkPrimer(int cx, int cz) {
        for (ChunkPrimer cp : cache) {
            if (ChunkPos.positionEquals(cp.chunkX, cp.chunkZ, cx, cz))
                return cp;
        }

        ChunkPrimer chunkPrimer = terrainGenerator.generate(cx, cz);
        
        if (cache.size() >= CACHE_LIMIT)
            cache.remove(cache.size() - 1);
        
        cache.add(0, chunkPrimer);
        return chunkPrimer;
    }
    
    public short getBlockID(int x, int y, int z) {
        int cx = ChunkPos.toChunkX(x);
        int cz = ChunkPos.toChunkZ(z);

        ChunkPrimer chunkPrimer = getChunkPrimer(cx, cz);
        
        int localX = ChunkPos.toLocalX(cx, x);
        int localZ = ChunkPos.toLocalZ(cz, z);

        return chunkPrimer.getBlockID(localX, y, localZ);
    }
}