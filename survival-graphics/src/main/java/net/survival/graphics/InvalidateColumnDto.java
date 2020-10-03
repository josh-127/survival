package net.survival.graphics;

import net.survival.block.Column;

class InvalidateColumnDto {
    private final long columnPos;
    private final Column column;
    private final ColumnInvalidationPriority invalidationPriority;

    public InvalidateColumnDto(
            long columnPos,
            Column column,
            ColumnInvalidationPriority invalidationPriority)
    {
        this.columnPos = columnPos;
        this.column = column;
        this.invalidationPriority = invalidationPriority;
    }

    public long getColumnPos() {
        return columnPos;
    }

    public Column getColumn() {
        return column;
    }

    public ColumnInvalidationPriority getInvalidationPriority() {
        return invalidationPriority;
    }
}