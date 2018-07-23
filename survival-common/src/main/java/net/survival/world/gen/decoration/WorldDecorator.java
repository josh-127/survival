package net.survival.world.gen.decoration;

import net.survival.world.World;
import net.survival.world.chunk.Chunk;

public interface WorldDecorator
{
    void decorate(int cx, int cz, Chunk chunk, World world);

    public static WorldDecorator createDefault() {
        return new SmallTreeDecorator();
    }
}