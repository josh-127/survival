package net.survival.world.chunk;

public interface ChunkGenerator
{
    Chunk generate(int cx, int cy, int cz);
}