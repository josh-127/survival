package net.survival.world.gen.v1;

import net.survival.world.chunk.Chunk;
import net.survival.world.chunk.ChunkGenerator;
import net.survival.world.gen.v1.terrain.CachedTerrainGenerator;

public class V1ChunkGenerator implements ChunkGenerator
{
    private final CachedTerrainGenerator terrainGenerator;
    
    public V1ChunkGenerator(long seed) {
        terrainGenerator = new CachedTerrainGenerator(seed);
    }

    @Override
    public Chunk generate(int cx, int cy, int cz) {
        return terrainGenerator.generate(cx, cy, cz).toChunk();
    }
}