package net.survival.world.gen.decoration;

import net.survival.world.World;
import net.survival.world.column.Column;

public interface WorldDecorator
{
    void decorate(int cx, int cz, Column column, World world);

    public static WorldDecorator createDefault() {
        return new SmallTreeDecorator();
    }
}