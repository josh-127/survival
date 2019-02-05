package net.survival.block;

public class ColumnRequest
{
    public static final byte TYPE_GET = 1;
    public static final byte TYPE_POST = 2;
    public static final byte TYPE_CLOSE = 3;

    public final long columnPos;
    public final Column column;
    public final byte type;

    private ColumnRequest(long columnPos, Column column, byte type) {
        this.columnPos = columnPos;
        this.column = column;
        this.type = type;
    }

    public static ColumnRequest createGetRequest(long columnPos) {
        return new ColumnRequest(columnPos, null, TYPE_GET);
    }

    public static ColumnRequest createGetRequest(int cx, int cz) {
        return createGetRequest(ColumnPos.hashPos(cx, cz));
    }

    public static ColumnRequest createPostRequest(long columnPos, Column column) {
        return new ColumnRequest(columnPos, column.makeCopy(), TYPE_POST);
    }

    public static ColumnRequest createPostRequest(int cx, int cz, Column column) {
        return createPostRequest(ColumnPos.hashPos(cx, cz), column);
    }

    public static ColumnRequest createCloseRequest() {
        return new ColumnRequest(0L, null, TYPE_CLOSE);
    }
}