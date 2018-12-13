package net.survival.client.graphics;

import net.survival.world.chunk.ChunkPos;

public interface RenderContext
{
    void redrawChunk(long hashedPos);

    default void redrawChunk(int cx, int cz) {
        redrawChunk(ChunkPos.hashPos(cx, cz));
    }
}