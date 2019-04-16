package net.survival.gen;

import net.survival.block.Column;
import net.survival.block.ColumnPos;
import net.survival.block.ColumnProvider;
import net.survival.gen.decoration.DecoratorContext;
import net.survival.gen.decoration.DefaultColumnDecorator;
import net.survival.gen.layer.GenLayer;
import net.survival.gen.layer.GenLayerFactory;
import net.survival.gen.terrain.FlatTerrainGenerator;
import net.survival.gen.terrain.TerrainContext;

public class FlatBiomeColumnGenerator implements ColumnProvider
{
    private final GenLayer biomeMap;
    private final FlatTerrainGenerator terrainGenerator;
    private final DefaultColumnDecorator decorator;

    private final ColumnPrimer primer;

    public FlatBiomeColumnGenerator(long seed) {
        biomeMap = GenLayerFactory.createBiomeLayer(
                ColumnPrimer.XLENGTH * 4,
                ColumnPrimer.ZLENGTH * 4,
                seed);

        terrainGenerator = new FlatTerrainGenerator();
        decorator = new DefaultColumnDecorator(false);

        primer = new ColumnPrimer();
    }

    @Override
    public Column provideColumn(long hashedPos) {
        var cx = ColumnPos.columnXFromHashedPos(hashedPos);
        var cz = ColumnPos.columnZFromHashedPos(hashedPos);
        var globalX = ColumnPos.toGlobalX(cx, 0);
        var globalZ = ColumnPos.toGlobalZ(cz, 0);

        primer.clear();
        biomeMap.generate(globalX, globalZ);
        terrainGenerator.generate(new TerrainContext(hashedPos, primer, biomeMap));
        decorator.decorate(new DecoratorContext(hashedPos, primer, biomeMap));

        return primer.toColumn();
    }
}