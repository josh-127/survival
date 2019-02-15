package net.survival.gen;

import net.survival.gen.layer.GenLayer;

class DefaultColumnDecorator extends ColumnDecorator
{
    private final CactusDecorator cactusDecorator = new CactusDecorator();

    @Override
    public void decorate(long columnPos, ColumnPrimer primer, GenLayer biomeMap) {
        cactusDecorator.decorate(columnPos, primer, biomeMap);
    }
}