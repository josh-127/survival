package net.survival.client;

import java.util.HashSet;
import java.util.Set;

import net.survival.block.ColumnPos;

public final class ColumnMaskFactory {
    private ColumnMaskFactory() {}

    public static Set<Long> createCircle(int radius, double offsetX, double offsetZ) {
        var radiusSquared = radius * radius;
        var offsetCX = ColumnPos.toColumnX((int) Math.floor(offsetX));
        var offsetCZ = ColumnPos.toColumnZ((int) Math.floor(offsetZ));
        var mask = new HashSet<Long>((int) Math.ceil(Math.PI * radiusSquared));

        for (var z = -radius; z < radius; ++z) {
            for (var x = -radius; x < radius; ++x) {
                if (squareDistance(x, z) <= radiusSquared) {
                    var cx = offsetCX + x;
                    var cz = offsetCZ + z;
                    mask.add(ColumnPos.hashPos(cx, cz));
                }
            }
        }

        return mask;
    }

    private static int squareDistance(int x, int z) {
        return (x * x) + (z * z);
    }
}