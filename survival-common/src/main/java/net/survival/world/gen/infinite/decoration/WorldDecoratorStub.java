package net.survival.world.gen.infinite.decoration;

import net.survival.world.chunk.Chunk;
import net.survival.world.gen.infinite.terrain.WorldTerrain;

public class WorldDecoratorStub implements WorldDecorator
{
    @Override
    public Chunk decorate(int cx, int cy, int cz, WorldTerrain worldTerrain) {
        return worldTerrain.getChunkPrimer(cx, cy, cz).toChunk();
    }
}