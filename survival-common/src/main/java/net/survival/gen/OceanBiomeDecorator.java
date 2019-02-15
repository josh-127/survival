package net.survival.gen;

import java.util.Random;

import net.survival.blocktype.BlockType;
import net.survival.gen.layer.GenLayer;

class OceanBiomeDecorator extends ColumnDecorator
{
    private static final int MIN_DIRT_DEPTH = 2;
    private static final int MAX_DIRT_DEPTH = 4;
    private static final int DIRT_DEPTH_RANGE = MAX_DIRT_DEPTH - MIN_DIRT_DEPTH;

    private final Random random = new Random();

    private final int bedrockID = BlockType.BEDROCK.getFullID();
    private final int stoneID = BlockType.STONE.getFullID();
    private final int dirtID = BlockType.DIRT.getFullID();
    private final int tempSolidID = BlockType.TEMP_SOLID.getFullID();

    @Override
    public void decorate(long columnPos, ColumnPrimer primer, GenLayer biomeMap) {
        random.setSeed(columnPos);

        for (var z = 0; z < ColumnPrimer.ZLENGTH; ++z) {
            for (var x = 0; x < ColumnPrimer.XLENGTH; ++x) {
                if (biomeMap.sampleNearest(x, z) == BiomeType.OCEAN.ordinal())
                    decorateStrip(x, z, primer);
            }
        }
    }

    private void decorateStrip(int x, int z, ColumnPrimer primer) {
        primer.setBlockFullID(x, 0, z, bedrockID);

        var state = 0;
        var counter = MIN_DIRT_DEPTH + random.nextInt(DIRT_DEPTH_RANGE + 1);

        for (var y = primer.getTopLevel(x, z); y >= 1; --y) {
            if (primer.getBlockFullID(x, y, z) != tempSolidID) {
                state = 0;
                counter = MIN_DIRT_DEPTH + random.nextInt(DIRT_DEPTH_RANGE + 1);
                continue;
            }

            switch (state) {
            case 0:
                primer.setBlockFullID(x, y, z, dirtID);
                ++state;
                break;

            case 1:
                if (counter > 0) {
                    primer.setBlockFullID(x, y, z, dirtID);
                    --counter;

                    if (counter == 0) {
                        ++state;
                        counter = 8;
                    }
                }

                break;

            case 2:
                primer.setBlockFullID(x, y, z, stoneID);
                break;
            }
        }
    }
}