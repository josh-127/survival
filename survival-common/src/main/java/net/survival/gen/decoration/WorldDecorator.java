package net.survival.gen.decoration;

import net.survival.block.BlockSpace;
import net.survival.block.column.Column;

public interface WorldDecorator
{
    void decorate(int cx, int cz, Column column, BlockSpace blockSpace);

    public static WorldDecorator createDefault() {
        return new SmallTreeDecorator();
    }
}