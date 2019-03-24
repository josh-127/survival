package net.survival.gen;

import java.util.Random;

import net.survival.blocktype.BlockType;
import net.survival.gen.layer.GenLayer;

class GrasslandBiomeDecorator extends ColumnDecorator
{
    private static final int MIN_DIRT_DEPTH = 2;
    private static final int MAX_DIRT_DEPTH = 4;
    private static final int DIRT_DEPTH_RANGE = MAX_DIRT_DEPTH - MIN_DIRT_DEPTH;

    private final Random random = new Random();

    private final int bedrockId = BlockType.BEDROCK.getFullId();
    private final int stoneId = BlockType.STONE.getFullId();
    private final int dirtId = BlockType.DIRT.getFullId();
    private final int grassId = BlockType.GRASS_BLOCK.getFullId();
    private final int tempSolidId = BlockType.TEMP_SOLID.getFullId();

    @Override
    public void decorate(long columnPos, ColumnPrimer primer, GenLayer biomeMap) {
        random.setSeed(columnPos);

        for (var z = 0; z < ColumnPrimer.ZLENGTH; ++z) {
            for (var x = 0; x < ColumnPrimer.XLENGTH; ++x) {
                if (biomeMap.sampleNearest(x, z) == BiomeType.GRASSLAND.ordinal())
                    decorateStrip(x, z, primer);
            }
        }
    }

    private void decorateStrip(int x, int z, ColumnPrimer primer) {
        primer.setBlockFullId(x, 0, z, bedrockId);

        var state = 0;
        var counter = MIN_DIRT_DEPTH + random.nextInt(DIRT_DEPTH_RANGE + 1);

        for (var y = primer.getTopLevel(x, z); y >= 1; --y) {
            if (primer.getBlockFullId(x, y, z) != tempSolidId) {
                state = 0;
                counter = MIN_DIRT_DEPTH + random.nextInt(DIRT_DEPTH_RANGE + 1);
                continue;
            }

            switch (state) {
            case 0:
                primer.setBlockFullId(x, y, z, grassId);
                ++state;
                break;

            case 1:
                if (counter > 0) {
                    primer.setBlockFullId(x, y, z, dirtId);
                    --counter;

                    if (counter == 0) {
                        ++state;
                        counter = 8;
                    }
                }

                break;

            case 2:
                primer.setBlockFullId(x, y, z, stoneId);
                break;
            }
        }
    }
}