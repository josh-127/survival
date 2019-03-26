package net.survival.gen;

import net.survival.blocktype.BlockType;

class DesertBiomeDecorator extends ColumnDecorator
{
    private final int stoneId = BlockType.STONE.getFullId();
    // TODO: Generate sandstone.
    private final int sandId = BlockType.SAND.getFullId();
    private final int tempSolidId = BlockType.TEMP_SOLID.getFullId();

    @Override
    public void decorate(DecoratorContext context) {
        var primer = context.getColumnPrimer();
        var biomeMap = context.getBiomeMap();

        for (var z = 0; z < ColumnPrimer.ZLENGTH; ++z) {
            for (var x = 0; x < ColumnPrimer.XLENGTH; ++x) {
                if (biomeMap.sampleNearest(x, z) == BiomeType.DESERT.ordinal())
                    decorateStrip(x, z, primer);
            }
        }
    }

    private void decorateStrip(int x, int z, ColumnPrimer primer) {
        var state = 0;
        var counter = 3;

        for (var y = primer.getTopLevel(x, z); y >= 1; --y) {
            if (primer.getBlockFullId(x, y, z) != tempSolidId) {
                state = 0;
                counter = 3;
                continue;
            }

            switch (state) {
            case 0:
                primer.setBlockFullId(x, y, z, sandId);
                ++state;
                break;

            case 1:
                if (counter > 0) {
                    primer.setBlockFullId(x, y, z, sandId);
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