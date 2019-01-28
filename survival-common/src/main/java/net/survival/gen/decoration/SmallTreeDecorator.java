package net.survival.gen.decoration;

import java.util.Random;

import net.survival.block.BlockSpace;
import net.survival.block.column.Column;
import net.survival.block.column.ColumnPos;

public class SmallTreeDecorator implements WorldDecorator
{
    private final Random random;

    public SmallTreeDecorator() {
        random = new Random();
    }

    @Override
    public void decorate(int cx, int cz, Column column, BlockSpace blockSpace) {
        random.setSeed(ColumnPos.hashPos(cx, cz));

        /*
        for (var i = 0; i < 2; ++i)
            generateTree(cx, cz, column, world);
*/
    }

/*
    private void generateTree(int cx, int cz, Column column, BlockSpace blockSpace) {
        var originX = random.nextInt(Column.XLENGTH);
        var originZ = random.nextInt(Column.ZLENGTH);
        var groundY = column.getTopLevel(originX, originZ);

        if (column.getBlock(originX, groundY, originZ) != BlockType.GRASS.id)
            return;
        
        column.setBlock(originX, groundY, originZ, BlockType.DIRT.id);

        var globalX = ColumnPos.toGlobalX(cx, originX);
        var globalZ = ColumnPos.toGlobalZ(cz, originZ);

        final var RADIUS = 5;
        final var HEIGHT = 10;

        for (var y = 0; y <= HEIGHT; ++y) {
            for (var z = -RADIUS; z <= RADIUS; ++z) {
                for (var x = -RADIUS; x <= RADIUS; ++x) {
                    if (x * x + y * y + z * z > RADIUS * RADIUS)
                        continue;

                    var gx = globalX + x;
                    var gy = groundY + y + 4;
                    var gz = globalZ + z;

                    if (gy < Column.YLENGTH)
                        blockSpace.placeBlockIfEmpty(gx, gy, gz, BlockType.OAK_LEAVES.id);
                }
            }
        }

        for (var y = groundY + 1; y <= groundY + 8; ++y) {
            if (y < Column.YLENGTH)
                column.setBlock(originX, y, originZ, BlockType.OAK_LOG.id);
        }
    }
*/
}