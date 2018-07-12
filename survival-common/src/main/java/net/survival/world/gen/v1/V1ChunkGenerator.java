package net.survival.world.gen.v1;

import net.survival.world.chunk.Chunk;
import net.survival.world.chunk.ChunkGenerator;
import net.survival.world.gen.v1.decoration.WorldDecoratorStub;
import net.survival.world.gen.v1.terrain.WorldTerrain;

public class V1ChunkGenerator implements ChunkGenerator
{
    private final WorldTerrain worldTerrain;
    private final WorldDecoratorStub worldDecoratorStub;
    
    public V1ChunkGenerator(long seed) {
        worldTerrain = new WorldTerrain(seed);
        worldDecoratorStub = new WorldDecoratorStub();
    }

    @Override
    public Chunk generate(int cx, int cy, int cz) {
        Chunk chunk = worldDecoratorStub.decorate(cx, cy, cz, worldTerrain);
        return chunk;
    }
}