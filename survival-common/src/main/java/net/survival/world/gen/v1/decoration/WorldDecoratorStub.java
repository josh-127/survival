package net.survival.world.gen.v1.decoration;

import net.survival.world.chunk.Chunk;
import net.survival.world.gen.v1.terrain.WorldTerrain;

public class WorldDecoratorStub implements WorldDecorator
{
    @Override
    public Chunk decorate(int cx, int cy, int cz, WorldTerrain worldTerrain) {
        return worldTerrain.getChunkPrimer(cx, cy, cz).toChunk();
    }
}