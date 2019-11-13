package net.survival.gen.decoration;

import net.survival.block.state.WaterBlock;
import net.survival.gen.ColumnPrimer;

public class WaterDecorator extends ColumnDecorator
{
    private static final int SEA_LEVEL = 63;

    @Override
    public void decorate(DecoratorContext context) {
        var primer = context.getColumnPrimer();

        for (int x = 0; x < ColumnPrimer.XLENGTH; ++x) {
            for (int z = 0; z < ColumnPrimer.ZLENGTH; ++z) {
                var surfaceY = primer.getTopLevel(x, z);
                if (surfaceY < 0) {
                    continue;
                }

                for (int y = surfaceY + 1; y <= SEA_LEVEL; ++y) {
                    primer.setBlock(x, y, z, WaterBlock.INSTANCE);
                }
            }
        }
    }
}