package net.survival.gen;

import net.survival.block.Column;
import net.survival.gen.layer.GenLayer;

class DefaultColumnDecorator extends ColumnDecorator
{
    private final CactusDecorator cactusDecorator = new CactusDecorator();

    @Override
    public void decorate(long columnPos, Column column, GenLayer biomeMap) {
        cactusDecorator.decorate(columnPos, column, biomeMap);
    }
}