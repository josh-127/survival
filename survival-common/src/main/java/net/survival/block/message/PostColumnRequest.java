package net.survival.block.message;

import net.survival.block.Column;

public class PostColumnRequest extends ColumnRequest
{
    private final long columnPos;
    private final Column column;

    public PostColumnRequest(long columnPos, Column column) {
        this.columnPos = columnPos;
        this.column = column;
    }

    public long getColumnPos() {
        return columnPos;
    }

    public Column getColumn() {
        return column;
    }

    @Override
    public void accept(ColumnRequestVisitor visitor) {
        visitor.visit(this);
    }
}