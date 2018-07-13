package net.survival.world.gen.infinite;

import net.survival.world.chunk.Chunk;
import net.survival.world.chunk.ChunkGenerator;
import net.survival.world.gen.infinite.terrain.WorldTerrain;

public class InfiniteChunkGenerator implements ChunkGenerator
{
    private final WorldTerrain worldTerrain;
    
    public InfiniteChunkGenerator(long seed) {
        worldTerrain = new WorldTerrain(seed);
    }

    @Override
    public Chunk generate(int cx, int cz) {
        return worldTerrain.getChunkPrimer(cx, cz).toChunk();
    }
}