package net.survival.world.chunk;

public class ChunkResponse
{
    public final long chunkPos;
    public final ChunkColumn chunkColumn;

    public ChunkResponse(long chunkPos, ChunkColumn chunkColumn) {
        this.chunkPos = chunkPos;
        this.chunkColumn = chunkColumn;
    }
}