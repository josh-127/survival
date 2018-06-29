package net.survival.world.chunk;

public interface ChunkProvider
{
    Chunk getChunk(int cx, int cy, int cz);
}