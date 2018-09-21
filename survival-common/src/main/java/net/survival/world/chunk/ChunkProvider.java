package net.survival.world.chunk;

public interface ChunkProvider
{
    Chunk provideChunk(long hashedPos);

    default Chunk provideChunk(int cx, int cz) {
        return provideChunk(ChunkPos.hashPos(cx, cz));
    }
}