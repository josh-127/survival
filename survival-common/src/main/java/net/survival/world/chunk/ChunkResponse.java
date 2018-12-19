package net.survival.world.chunk;

public class ChunkResponse
{
    public final long chunkPos;
    public final Chunk chunk;

    public ChunkResponse(long chunkPos, Chunk chunk) {
        this.chunkPos = chunkPos;
        this.chunk = chunk;
    }
}