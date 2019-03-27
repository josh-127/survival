package net.survival.gen;

import java.util.Random;

import net.survival.blocktype.BlockType;

class CactusDecorator extends ColumnDecorator
{
    private static final int CACTI_PER_COLUMN = 1;
    private static final int MIN_CACTUS_HEIGHT = 2;
    private static final int MAX_CACTUS_HEIGHT = 4;
    private static final int CACTUS_HEIGHT_RANGE = MAX_CACTUS_HEIGHT - MIN_CACTUS_HEIGHT;

    private final Random random = new Random();
    private final int sandFullId = BlockType.SAND.getFullId();
    private final int cactusFullId = BlockType.CACTUS.getFullId();

    @Override
    public void decorate(DecoratorContext context) {
        var columnPos = context.getColumnPos();
        var primer = context.getColumnPrimer();
        var biomeMap = context.getBiomeMap();

        random.setSeed(columnPos);

        for (var i = 0; i < CACTI_PER_COLUMN; ++i) {
            var x = random.nextInt(ColumnPrimer.XLENGTH);
            var z = random.nextInt(ColumnPrimer.ZLENGTH);

            if (biomeMap.sampleNearest(x, z) == BiomeType.DESERT.ordinal()) {
                var height = MIN_CACTUS_HEIGHT + random.nextInt(CACTUS_HEIGHT_RANGE + 1);
                var topLevel = primer.getTopLevel(x, z);

                if (topLevel != -1 && primer.getBlockFullId(x, topLevel, z) == sandFullId) {
                    for (var j = 1; j <= height && j < ColumnPrimer.YLENGTH; ++j) {
                        primer.setBlockFullId(x, topLevel + j, z, cactusFullId);
                    }
                }
            }
        }
    }
}