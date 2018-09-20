package net.survival.world.chunk;

import net.survival.concurrent.VoidCoroutineTask;

public interface PersistentChunkStorage extends AsyncChunkProvider
{
    VoidCoroutineTask moveAndSaveChunkAsync(int cx, int cz, Chunk chunk);

    default VoidCoroutineTask saveChunkAsync(int cx, int cz, Chunk chunk) {
        return moveAndSaveChunkAsync(cx, cz, chunk.makeCopy());
    }
}