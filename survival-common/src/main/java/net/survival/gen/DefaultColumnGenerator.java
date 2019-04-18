package net.survival.gen;

import net.survival.block.Column;
import net.survival.block.ColumnProvider;
import net.survival.gen.decoration.DecoratorContext;
import net.survival.gen.decoration.DefaultColumnDecorator;
import net.survival.gen.terrain.DefaultTerrainGenerator;
import net.survival.gen.terrain.TerrainContext;

public class DefaultColumnGenerator implements ColumnProvider
{
    private final DefaultTerrainGenerator terrainGenerator;
    private final DefaultColumnDecorator decorator;

    private final ColumnPrimer primer;

    public DefaultColumnGenerator(long seed) {
        terrainGenerator = new DefaultTerrainGenerator(seed);
        decorator = new DefaultColumnDecorator(false);

        primer = new ColumnPrimer();
    }

    @Override
    public Column provideColumn(long hashedPos) {
        primer.clear();
        terrainGenerator.generate(new TerrainContext(hashedPos, primer));
        decorator.decorate(new DecoratorContext(hashedPos, primer));

        return primer.toColumn();
    }
}