package net.survival.gen;

import net.survival.block.ColumnPos;
import net.survival.gen.layer.GenLayer;

class DecoratorContext
{
    private final long columnPos;
    private final ColumnPrimer primer;
    private final GenLayer biomeMap;

    public DecoratorContext(long columnPos, ColumnPrimer primer, GenLayer biomeMap) {
        this.columnPos = columnPos;
        this.primer = primer;
        this.biomeMap = biomeMap;
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

    public GenLayer getBiomeMap() {
        return biomeMap;
    }
}