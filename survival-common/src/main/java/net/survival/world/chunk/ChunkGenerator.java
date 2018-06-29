package net.survival.world.chunk;

public interface ChunkGenerator
{
    void generateTerrain(Chunk chunk);
    void populate(Chunk chunk);
}