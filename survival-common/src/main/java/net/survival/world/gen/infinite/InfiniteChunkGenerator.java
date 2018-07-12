package net.survival.world.gen.infinite;

import net.survival.world.chunk.Chunk;
import net.survival.world.chunk.ChunkGenerator;
import net.survival.world.gen.infinite.decoration.WorldDecoratorStub;
import net.survival.world.gen.infinite.terrain.WorldTerrain;

public class InfiniteChunkGenerator implements ChunkGenerator
{
    private final WorldTerrain worldTerrain;
    private final WorldDecoratorStub worldDecoratorStub;
    
    public InfiniteChunkGenerator(long seed) {
        worldTerrain = new WorldTerrain(seed);
        worldDecoratorStub = new WorldDecoratorStub();
    }

    @Override
    public Chunk generate(int cx, int cz) {
        Chunk chunk = worldDecoratorStub.decorate(cx, cz, worldTerrain);
        return chunk;
    }
}