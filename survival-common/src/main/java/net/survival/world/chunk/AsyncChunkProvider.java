package net.survival.world.chunk;

import net.survival.concurrent.Coroutine;

public interface AsyncChunkProvider
{
    Coroutine<Chunk> provideChunkAsync(long hashedPos);

    default Coroutine<Chunk> provideChunkAsync(int cx, int cz) {
        return provideChunkAsync(ChunkPos.hashPos(cx, cz));
    }
}