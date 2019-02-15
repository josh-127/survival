package net.survival.gen;

import net.survival.gen.layer.GenLayer;

class DefaultColumnDecorator extends ColumnDecorator
{
    private final OceanBiomeDecorator oceanBiomeDecorator = new OceanBiomeDecorator();
    private final DesertBiomeDecorator desertBiomeDecorator = new DesertBiomeDecorator();
    private final CactusDecorator cactusDecorator = new CactusDecorator();

    @Override
    public void decorate(long columnPos, ColumnPrimer primer, GenLayer biomeMap) {
        oceanBiomeDecorator.decorate(columnPos, primer, biomeMap);
        desertBiomeDecorator.decorate(columnPos, primer, biomeMap);
        cactusDecorator.decorate(columnPos, primer, biomeMap);
    }
}