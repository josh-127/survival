package net.survival.world.chunk;

public interface ChunkGenerator
{
    void generateTerrain(int cx, int cy, int cz, Chunk chunk);
    void populate(int cx, int cy, int cz, Chunk chunk);
}