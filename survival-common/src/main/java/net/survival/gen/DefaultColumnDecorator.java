package net.survival.gen;

class DefaultColumnDecorator extends ColumnDecorator
{
    private final BedrockDecorator bedrockDecorator = new BedrockDecorator();
    private final DesertBiomeDecorator desertBiomeDecorator = new DesertBiomeDecorator();
    private final ExtremeHillsDecorator extremeHillsDecorator = new ExtremeHillsDecorator();
    private final ForestBiomeDecorator forestBiomeDecorator = new ForestBiomeDecorator();
    private final GrasslandBiomeDecorator grasslandBiomeDecorator = new GrasslandBiomeDecorator();
    private final OceanBiomeDecorator oceanBiomeDecorator = new OceanBiomeDecorator();
    private final CactusDecorator cactusDecorator = new CactusDecorator();
    private final SaplingDecorator saplingDecorator = new SaplingDecorator();

    @Override
    public void decorate(DecoratorContext context) {
        bedrockDecorator.decorate(context);
        desertBiomeDecorator.decorate(context);
        extremeHillsDecorator.decorate(context);
        forestBiomeDecorator.decorate(context);
        grasslandBiomeDecorator.decorate(context);
        oceanBiomeDecorator.decorate(context);
        cactusDecorator.decorate(context);
        saplingDecorator.decorate(context);
    }
}