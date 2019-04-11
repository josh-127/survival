package net.survival.gen.decoration;

import net.survival.blocktype.BlockType;
import net.survival.gen.ColumnPrimer;

class BedrockDecorator extends ColumnDecorator
{
    private final int bedrockId = BlockType.BEDROCK.getFullId();

    @Override
    public void decorate(DecoratorContext context) {
        var primer = context.getColumnPrimer();

        for (int z = 0; z < ColumnPrimer.ZLENGTH; ++z) {
            for (int x = 0; x < ColumnPrimer.XLENGTH; ++x) {
                primer.setBlockFullId(x, 0, z, bedrockId);
            }
        }
    }
}