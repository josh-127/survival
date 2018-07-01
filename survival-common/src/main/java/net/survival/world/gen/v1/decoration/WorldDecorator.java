package net.survival.world.gen.v1.decoration;

import net.survival.world.World;
import net.survival.world.chunk.Chunk;
import net.survival.world.chunk.ChunkProvider;

public abstract class WorldDecorator
{
    public abstract void decorate(World world, Chunk chunk, ChunkProvider chunkProvider);
}