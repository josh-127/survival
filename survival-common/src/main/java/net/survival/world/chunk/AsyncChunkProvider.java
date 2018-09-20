package net.survival.world.chunk;

import net.survival.concurrent.CoroutineTask;

public interface AsyncChunkProvider
{
    CoroutineTask<Chunk> provideChunkAsync(long hashedPos);

    default CoroutineTask<Chunk> provideChunkAsync(int cx, int cz) {
        return provideChunkAsync(ChunkPos.hashPos(cx, cz));
    }
}