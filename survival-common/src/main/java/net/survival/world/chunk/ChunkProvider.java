package net.survival.world.chunk;

public interface ChunkProvider
{
    ChunkColumn provideChunk(long hashedPos);

    default ChunkColumn provideChunk(int cx, int cz) {
        return provideChunk(ChunkColumnPos.hashPos(cx, cz));
    }
}