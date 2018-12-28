package net.survival.world.chunk;

public class ChunkRequest
{
    public static final byte TYPE_GET = 1;
    public static final byte TYPE_POST = 2;
    public static final byte TYPE_CLOSE = 3;

    public final long chunkPos;
    public final ChunkColumn chunkColumn;
    public final byte type;

    private ChunkRequest(long chunkPos, ChunkColumn chunkColumn, byte type) {
        this.chunkPos = chunkPos;
        this.chunkColumn = chunkColumn;
        this.type = type;
    }

    public static ChunkRequest createGetRequest(long chunkPos) {
        return new ChunkRequest(chunkPos, null, TYPE_GET);
    }

    public static ChunkRequest createGetRequest(int cx, int cz) {
        return createGetRequest(ChunkColumnPos.hashPos(cx, cz));
    }

    public static ChunkRequest createPostRequest(long chunkPos, ChunkColumn chunkColumn) {
        return new ChunkRequest(chunkPos, chunkColumn.makeCopy(), TYPE_POST);
    }

    public static ChunkRequest createPostRequest(int cx, int cz, ChunkColumn chunkColumn) {
        return createPostRequest(ChunkColumnPos.hashPos(cx, cz), chunkColumn);
    }

    public static ChunkRequest createCloseRequest() {
        return new ChunkRequest(0L, null, TYPE_CLOSE);
    }
}