package net.survival.world.gen.infinite.decoration;

import net.survival.world.chunk.Chunk;
import net.survival.world.gen.infinite.terrain.WorldTerrain;

public interface WorldDecorator
{
    public abstract Chunk decorate(int cx, int cy, int cz, WorldTerrain worldTerrain);
}