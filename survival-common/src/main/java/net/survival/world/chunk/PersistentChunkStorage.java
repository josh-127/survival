package net.survival.world.chunk;

public interface PersistentChunkStorage extends AsyncChunkProvider
{
    void moveAndSaveChunkAsync(long hashedPos, Chunk chunk);

    default void moveAndSaveChunkAsync(int cx, int cz, Chunk chunk) {
        moveAndSaveChunkAsync(ChunkPos.hashPos(cx, cz), chunk);
    }

    default void saveChunkAsync(long hashedPos, Chunk chunk) {
        moveAndSaveChunkAsync(hashedPos, chunk.makeCopy());
    }

    default void saveChunkAsync(int cx, int cz, Chunk chunk) {
        moveAndSaveChunkAsync(cx, cz, chunk.makeCopy());
    }
}