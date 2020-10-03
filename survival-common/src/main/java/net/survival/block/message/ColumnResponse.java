package net.survival.block.message;

import net.survival.block.Column;

public class ColumnResponse {
    private final long columnPos;
    private final Column column;

    public ColumnResponse(long columnPos, Column column) {
        this.columnPos = columnPos;
        this.column = column;
    }

    public long getColumnPos() {
        return columnPos;
    }

    public Column getColumn() {
        return column;
    }
}