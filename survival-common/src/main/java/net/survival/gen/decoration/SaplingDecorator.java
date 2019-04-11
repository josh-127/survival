package net.survival.gen.decoration;

import java.util.Random;

import net.survival.blocktype.BlockType;
import net.survival.gen.BiomeType;
import net.survival.gen.ColumnPrimer;

class SaplingDecorator extends ColumnDecorator
{
    private static final int SAPLINGS_PER_COLUMN = 2;

    private final Random random = new Random();
    private final int sandFullId = BlockType.SAND.getFullId();
    private final int saplingFullId = BlockType.OAK_SAPLING.getFullId();

    @Override
    public void decorate(DecoratorContext context) {
        var columnPos = context.getColumnPos();
        var primer = context.getColumnPrimer();
        var biomeMap = context.getBiomeMap();

        random.setSeed(columnPos + 1000L);

        for (var i = 0; i < SAPLINGS_PER_COLUMN; ++i) {
            var x = random.nextInt(ColumnPrimer.XLENGTH);
            var z = random.nextInt(ColumnPrimer.ZLENGTH);

            if (biomeMap.sampleNearest(x, z) == BiomeType.DESERT.ordinal()) {
                var topLevel = primer.getTopLevel(x, z);

                if (topLevel != -1 &&
                        topLevel + 1 < ColumnPrimer.YLENGTH &&
                        primer.getBlockFullId(x, topLevel, z) == sandFullId)
                {
                    primer.setBlockFullId(x, topLevel + 1, z, saplingFullId);
                }
            }
        }
    }
}