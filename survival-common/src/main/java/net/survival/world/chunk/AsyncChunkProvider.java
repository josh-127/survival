package net.survival.world.chunk;

import net.survival.concurrent.DeferredResult;

public interface AsyncChunkProvider
{
    DeferredResult<Chunk> provideChunkAsync(long hashedPos);

    default DeferredResult<Chunk> provideChunkAsync(int cx, int cz) {
        return provideChunkAsync(ChunkPos.hashPos(cx, cz));
    }
}