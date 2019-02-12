package net.survival.block;

import java.util.HashSet;
import java.util.Set;

public class CircularColumnMask implements ColumnMask
{
    private final HashSet<Long> columnPositions;
    private final int radius;

    private int prevOffsetCX;
    private int prevOffsetCZ;

    public CircularColumnMask(int radius) {
        columnPositions = new HashSet<>(radius * radius);
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

        var radiusSquared = radius * radius;

        for (var z = -radius; z < radius; ++z) {
            for (var x = -radius; x < radius; ++x) {
                if (squareDistance(x, z) <= radiusSquared) {
                    var cx = offsetCX + x;
                    var cz = offsetCZ + z;
                    columnPositions.add(ColumnPos.hashPos(cx, cz));
                }
            }
        }
    }

    @Override
    public Set<Long> getColumnPositions() {
        return new HashSet<>(columnPositions);
    }

    private int squareDistance(int x, int z) {
        return (x * x) + (z * z);
    }
}