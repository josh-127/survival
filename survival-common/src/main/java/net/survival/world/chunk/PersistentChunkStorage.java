package net.survival.world.chunk;

import net.survival.concurrent.VoidCoroutine;

public interface PersistentChunkStorage extends AsyncChunkProvider
{
    VoidCoroutine moveAndSaveChunkAsync(long hashedPos, Chunk chunk);

    default VoidCoroutine moveAndSaveChunkAsync(int cx, int cz, Chunk chunk) {
        return moveAndSaveChunkAsync(ChunkPos.hashPos(cx, cz), chunk);
    }

    default VoidCoroutine saveChunkAsync(long hashedPos, Chunk chunk) {
        return moveAndSaveChunkAsync(hashedPos, chunk.makeCopy());
    }

    default VoidCoroutine saveChunkAsync(int cx, int cz, Chunk chunk) {
        return moveAndSaveChunkAsync(cx, cz, chunk.makeCopy());
    }
}