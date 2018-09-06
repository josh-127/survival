package net.survival.world.chunk;

public interface ChunkProvider
{
    Chunk provideChunk(int cx, int cz);
}