package net.survival.world.chunk;

public interface AsyncChunkProvider
{
    Chunk provideChunkAsync(int cx, int cz);
}