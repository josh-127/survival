package net.survival.gen;

import net.survival.blocktype.BlockType;

public class WaterDecorator extends ColumnDecorator
{
    private static final int SEA_LEVEL = 63;

    private final int waterId = BlockType.WATER.getFullId();

    @Override
    public void decorate(DecoratorContext context) {
        var primer = context.getColumnPrimer();

        for (int x = 0; x < ColumnPrimer.XLENGTH; ++x) {
            for (int z = 0; z < ColumnPrimer.ZLENGTH; ++z) {
                var surfaceY = primer.getTopLevel(x, z);
                if (surfaceY < 0) {
                    continue;
                }

                for (int y = surfaceY; y <= SEA_LEVEL; ++y) {
                    primer.setBlockFullId(x, y, z, waterId);
                }
            }
        }
    }
}