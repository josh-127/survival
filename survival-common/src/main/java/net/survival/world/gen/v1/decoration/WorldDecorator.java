package net.survival.world.gen.v1.decoration;

import net.survival.world.chunk.Chunk;
import net.survival.world.gen.v1.terrain.WorldTerrain;

public interface WorldDecorator
{
    public abstract Chunk decorate(int cx, int cy, int cz, WorldTerrain worldTerrain);
}