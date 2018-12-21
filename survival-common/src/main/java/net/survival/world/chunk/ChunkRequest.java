package net.survival.world.chunk;

public class ChunkRequest
{
    public static final byte TYPE_GET = 1;
    public static final byte TYPE_POST = 2;
    public static final byte TYPE_CLOSE = 3;

    public final long chunkPos;
    public final Chunk chunk;
    public final byte type;

    private ChunkRequest(long chunkPos, Chunk chunk, byte type) {
        this.chunkPos = chunkPos;
        this.chunk = chunk;
        this.type = type;
    }

    public static ChunkRequest createGetRequest(long chunkPos) {
        return new ChunkRequest(chunkPos, null, TYPE_GET);
    }

    public static ChunkRequest createGetRequest(int cx, int cz) {
        return createGetRequest(ChunkPos.hashPos(cx, cz));
    }

    public static ChunkRequest createPostRequest(long chunkPos, Chunk chunk) {
        return new ChunkRequest(chunkPos, chunk, TYPE_POST);
    }

    public static ChunkRequest createPostRequest(int cx, int cz, Chunk chunk) {
        return createPostRequest(ChunkPos.hashPos(cx, cz), chunk);
    }

    public static ChunkRequest createCloseRequest() {
        return new ChunkRequest(0L, null, TYPE_CLOSE);
    }
}