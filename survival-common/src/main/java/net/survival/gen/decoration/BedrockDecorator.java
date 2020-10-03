package net.survival.gen.decoration;

import net.survival.block.state.BedrockBlock;
import net.survival.gen.ColumnPrimer;

class BedrockDecorator extends ColumnDecorator {
    @Override
    public void decorate(DecoratorContext context) {
        var primer = context.getColumnPrimer();

        for (int z = 0; z < ColumnPrimer.ZLENGTH; ++z) {
            for (int x = 0; x < ColumnPrimer.XLENGTH; ++x) {
                primer.setBlock(x, 0, z, BedrockBlock.INSTANCE);
            }
        }
    }
}