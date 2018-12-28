package net.survival.world.gen.decoration;

import net.survival.world.World;
import net.survival.world.chunk.ChunkColumn;

public interface WorldDecorator
{
    void decorate(int cx, int cz, ChunkColumn chunkColumn, World world);

    public static WorldDecorator createDefault() {
        return new SmallTreeDecorator();
    }
}