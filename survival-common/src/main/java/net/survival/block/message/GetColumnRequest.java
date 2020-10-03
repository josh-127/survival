package net.survival.block.message;

public class GetColumnRequest extends ColumnRequest {
    private final long columnPos;

    public GetColumnRequest(long columnPos) {
        this.columnPos = columnPos;
    }

    public long getColumnPos() {
        return columnPos;
    }
}