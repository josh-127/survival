package net.survival.world.column;

public class ColumnResponse
{
    public final long columnPos;
    public final Column column;

    public ColumnResponse(long columnPos, Column column) {
        this.columnPos = columnPos;
        this.column = column;
    }
}