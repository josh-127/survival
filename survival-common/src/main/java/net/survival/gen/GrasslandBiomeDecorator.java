package net.survival.gen;

import net.survival.blocktype.BlockType;
import net.survival.gen.layer.GenLayer;

class GrasslandBiomeDecorator extends ColumnDecorator
{
    private final int bedrockID = BlockType.BEDROCK.getFullID();
    private final int stoneID = BlockType.STONE.getFullID();
    private final int dirtID = BlockType.DIRT.getFullID();
    private final int grassID = BlockType.GRASS_BLOCK.getFullID();
    private final int tempSolidID = BlockType.TEMP_SOLID.getFullID();

    @Override
    public void decorate(long columnPos, ColumnPrimer primer, GenLayer biomeMap) {
        for (var z = 0; z < ColumnPrimer.ZLENGTH; ++z) {
            for (var x = 0; x < ColumnPrimer.XLENGTH; ++x) {
                if (biomeMap.sampleNearest(x, z) == BiomeType.GRASSLAND.ordinal())
                    decorateStrip(x, z, primer);
            }
        }
    }

    private void decorateStrip(int x, int z, ColumnPrimer primer) {
        primer.setBlockFullID(x, 0, z, bedrockID);

        var state = 0;
        var counter = 3;

        for (var y = primer.getTopLevel(x, z); y >= 1; --y) {
            if (primer.getBlockFullID(x, y, z) != tempSolidID) {
                state = 0;
                counter = 3;
                continue;
            }

            switch (state) {
            case 0:
                primer.setBlockFullID(x, y, z, grassID);
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