package net.survival.gen;

import java.util.Random;

import net.survival.blocktype.BlockType;
import net.survival.gen.layer.GenLayer;

class CactusDecorator extends ColumnDecorator
{
    private static final int CACTI_PER_COLUMN = 5;

    private final Random random = new Random();
    private final int cactusFullID = BlockType.CACTUS.getFullID();

    @Override
    public void decorate(long columnPos, ColumnPrimer primer, GenLayer biomeMap) {
        random.setSeed(columnPos);

        for (var i = 0; i < CACTI_PER_COLUMN; ++i) {
            var x = random.nextInt(ColumnPrimer.XLENGTH);
            var z = random.nextInt(ColumnPrimer.ZLENGTH);

            if (biomeMap.sampleNearest(x, z) == BiomeType.DESERT.ordinal()) {
                primer.setBlockFullID(x, 80, z, cactusFullID);
            }
        }
    }
}