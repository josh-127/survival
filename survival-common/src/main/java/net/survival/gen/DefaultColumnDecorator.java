package net.survival.gen;

import net.survival.gen.layer.GenLayer;

class DefaultColumnDecorator extends ColumnDecorator
{
    private final DesertBiomeDecorator desertBiomeDecorator = new DesertBiomeDecorator();
    private final ExtremeHillsDecorator extremeHillsDecorator = new ExtremeHillsDecorator();
    private final ForestBiomeDecorator forestBiomeDecorator = new ForestBiomeDecorator();
    private final GrasslandBiomeDecorator grasslandBiomeDecorator = new GrasslandBiomeDecorator();
    private final OceanBiomeDecorator oceanBiomeDecorator = new OceanBiomeDecorator();
    private final CactusDecorator cactusDecorator = new CactusDecorator();
    private final SaplingDecorator saplingDecorator = new SaplingDecorator();

    @Override
    public void decorate(long columnPos, ColumnPrimer primer, GenLayer biomeMap) {
        desertBiomeDecorator.decorate(columnPos, primer, biomeMap);
        extremeHillsDecorator.decorate(columnPos, primer, biomeMap);
        forestBiomeDecorator.decorate(columnPos, primer, biomeMap);
        grasslandBiomeDecorator.decorate(columnPos, primer, biomeMap);
        oceanBiomeDecorator.decorate(columnPos, primer, biomeMap);
        cactusDecorator.decorate(columnPos, primer, biomeMap);
        saplingDecorator.decorate(columnPos, primer, biomeMap);
    }
}