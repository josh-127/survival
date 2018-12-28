package net.survival.world.column;

import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;

public class CircularColumnStageMask implements ColumnStageMask
{
    private final LongSet columnPositions;
    private final int radius;

    private int prevOffsetCX;
    private int prevOffsetCZ;

    public CircularColumnStageMask(int radius) {
        columnPositions = new LongOpenHashSet(radius * radius);
        this.radius = radius;

        forceSetCenter(0, 0);
    }

    public void setCenter(int offsetCX, int offsetCZ) {
        if (offsetCX == prevOffsetCX && offsetCZ == prevOffsetCZ)
            return;

        forceSetCenter(offsetCX, offsetCZ);
    }

    private void forceSetCenter(int offsetCX, int offsetCZ) {
        columnPositions.clear();

        int radiusSquared = radius * radius;

        for (int z = -radius; z < radius; ++z) {
            for (int x = -radius; x < radius; ++x) {
                if (squareDistance(x, z) <= radiusSquared) {
                    int cx = offsetCX + x;
                    int cz = offsetCZ + z;
                    columnPositions.add(ColumnPos.hashPos(cx, cz));
                }
            }
        }
    }

    @Override
    public LongSet getColumnPositions() {
        return new LongOpenHashSet(columnPositions);
    }

    private int squareDistance(int x, int z) {
        return (x * x) + (z * z);
    }
}