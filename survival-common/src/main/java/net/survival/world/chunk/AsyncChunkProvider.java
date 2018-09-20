package net.survival.world.chunk;

import net.survival.concurrent.CoroutineTask;

public interface AsyncChunkProvider
{
    CoroutineTask<Chunk> provideChunkAsync(int cx, int cz);
}