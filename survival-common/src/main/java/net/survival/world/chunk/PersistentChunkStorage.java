package net.survival.world.chunk;

import net.survival.concurrent.VoidCoroutineTask;

public interface PersistentChunkStorage extends AsyncChunkProvider
{
    VoidCoroutineTask moveAndSaveChunkAsync(long hashedPos, Chunk chunk);

    default VoidCoroutineTask moveAndSaveChunkAsync(int cx, int cz, Chunk chunk) {
        return moveAndSaveChunkAsync(ChunkPos.hashPos(cx, cz), chunk);
    }

    default VoidCoroutineTask saveChunkAsync(long hashedPos, Chunk chunk) {
        return moveAndSaveChunkAsync(hashedPos, chunk.makeCopy());
    }

    default VoidCoroutineTask saveChunkAsync(int cx, int cz, Chunk chunk) {
        return moveAndSaveChunkAsync(cx, cz, chunk.makeCopy());
    }
}