package net.survival.world.chunk;

public interface ChunkDatabase
{
    Chunk loadChunk(int cx, int cz);
}