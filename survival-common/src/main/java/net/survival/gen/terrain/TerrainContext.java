package net.survival.gen.terrain;

import net.survival.block.ColumnPos;
import net.survival.gen.ColumnPrimer;

public class TerrainContext {
    private final long columnPos;
    private final ColumnPrimer primer;

    public TerrainContext(long columnPos, ColumnPrimer primer) {
        this.columnPos = columnPos;
        this.primer = primer;
    }

    public long getColumnPos() {
        return columnPos;
    }

    public int getColumnX() {
        return ColumnPos.columnXFromHashedPos(columnPos);
    }

    public int getColumnZ() {
        return ColumnPos.columnZFromHashedPos(columnPos);
    }

    public ColumnPrimer getColumnPrimer() {
        return primer;
    }
}