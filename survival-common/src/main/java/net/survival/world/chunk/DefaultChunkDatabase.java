package net.survival.world.chunk;

import net.survival.concurrent.CoroutineTask;

public class DefaultChunkDatabase implements AsyncChunkProvider
{
    public DefaultChunkDatabase() {}

    @Override
    public CoroutineTask<Chunk> provideChunkAsync(int cx, int cz) {
        throw new RuntimeException("Not implemented yet.");
    }
}