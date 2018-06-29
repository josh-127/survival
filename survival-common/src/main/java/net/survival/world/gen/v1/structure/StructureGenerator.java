package net.survival.world.gen.v1.structure;

import net.survival.world.World;
import net.survival.world.chunk.Chunk;
import net.survival.world.chunk.ChunkProvider;

public abstract class StructureGenerator
{
    public abstract void generate(World world, Chunk chunk, ChunkProvider chunkProvider);
}