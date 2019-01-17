package net.survival.world.gen.decoration;

import net.survival.world.BlockSpace;
import net.survival.world.column.Column;

public interface WorldDecorator
{
    void decorate(int cx, int cz, Column column, BlockSpace blockSpace);

    public static WorldDecorator createDefault() {
        return new SmallTreeDecorator();
    }
}