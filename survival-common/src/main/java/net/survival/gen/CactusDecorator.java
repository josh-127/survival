package net.survival.gen;

import java.util.Random;

import net.survival.blocktype.BlockType;
import net.survival.gen.layer.GenLayer;

class CactusDecorator extends ColumnDecorator
{
    private static final int CACTI_PER_COLUMN = 5;
    private static final int MIN_CACTUS_HEIGHT = 2;
    private static final int MAX_CACTUS_HEIGHT = 4;
    private static final int CACTUS_HEIGHT_RANGE = MAX_CACTUS_HEIGHT - MIN_CACTUS_HEIGHT;

    private final Random random = new Random();
    private final int sandFullID = BlockType.SAND.getFullID();
    private final int cactusFullID = BlockType.CACTUS.getFullID();

    @Override
    public void decorate(long columnPos, ColumnPrimer primer, GenLayer biomeMap) {
        random.setSeed(columnPos);

        for (var i = 0; i < CACTI_PER_COLUMN; ++i) {
            var x = random.nextInt(ColumnPrimer.XLENGTH);
            var z = random.nextInt(ColumnPrimer.ZLENGTH);

            if (biomeMap.sampleNearest(x, z) == BiomeType.DESERT.ordinal()) {
                var height = MIN_CACTUS_HEIGHT + random.nextInt(CACTUS_HEIGHT_RANGE + 1);
                var topLevel = primer.getTopLevel(x, z);

                if (topLevel != -1 && primer.getBlockFullID(x, topLevel, z) == sandFullID) {
                    for (var j = 1; j <= height && j < ColumnPrimer.YLENGTH; ++j) {
                        primer.setBlockFullID(x, topLevel + j, z, cactusFullID);
                    }
                }
            }
        }
    }
}