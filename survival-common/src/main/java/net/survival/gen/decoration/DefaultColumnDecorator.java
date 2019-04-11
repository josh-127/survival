package net.survival.gen.decoration;

import net.survival.blocktype.BlockType;
import net.survival.gen.BiomeType;
import net.survival.gen.SurfaceDescription;
import net.survival.gen.SurfaceLayer;
import net.survival.gen.SurfaceDescription.Builder;

public class DefaultColumnDecorator extends ColumnDecorator
{
    private final int grassId = BlockType.GRASS_BLOCK.getFullId();
    private final int dirtId = BlockType.DIRT.getFullId();
    private final int sandId = BlockType.SAND.getFullId();
    private final int sandstoneId = BlockType.SANDSTONE.getFullId();

    private final BedrockDecorator bedrockDecorator = new BedrockDecorator();

    private final SurfaceDescription desertSurface = new SurfaceDescription.Builder()
            .addLayer(new SurfaceLayer(sandId, 3, 6))
            .addLayer(new SurfaceLayer(sandstoneId, 5, 8))
            .withBiomeType(BiomeType.DESERT)
            .build();
    private final SurfaceDecorator desertSurfaceDecorator = new SurfaceDecorator(desertSurface);

    private final SurfaceDescription extremeHillsSurface = new SurfaceDescription.Builder()
            .addLayer(new SurfaceLayer(grassId, 1, 1))
            .addLayer(new SurfaceLayer(dirtId, 3, 5))
            .withBiomeType(BiomeType.EXTREME_HILLS)
            .build();
    private final SurfaceDecorator extremeHillsSurfaceDecorator = new SurfaceDecorator(extremeHillsSurface);

    private final SurfaceDescription forestSurface = new SurfaceDescription.Builder()
            .addLayer(new SurfaceLayer(grassId, 1, 1))
            .addLayer(new SurfaceLayer(dirtId, 3, 5))
            .withBiomeType(BiomeType.FOREST)
            .build();
    private final SurfaceDecorator forestSurfaceDecorator = new SurfaceDecorator(forestSurface);

    private final SurfaceDescription grasslandSurface = new SurfaceDescription.Builder()
            .addLayer(new SurfaceLayer(grassId, 1, 1))
            .addLayer(new SurfaceLayer(dirtId, 3, 5))
            .withBiomeType(BiomeType.GRASSLAND)
            .build();
    private final SurfaceDecorator grasslandSurfaceDecorator = new SurfaceDecorator(grasslandSurface);

    private final SurfaceDescription oceanSurface = new SurfaceDescription.Builder()
            .addLayer(new SurfaceLayer(dirtId, 3, 6))
            .withBiomeType(BiomeType.OCEAN)
            .build();
    private final SurfaceDecorator oceanSurfaceDecorator = new SurfaceDecorator(oceanSurface);

    private final WaterDecorator waterDecorator = new WaterDecorator();

    private final CactusDecorator cactusDecorator = new CactusDecorator();
    private final SaplingDecorator saplingDecorator = new SaplingDecorator();

    @Override
    public void decorate(DecoratorContext context) {
        bedrockDecorator.decorate(context);
        desertSurfaceDecorator.decorate(context);
        extremeHillsSurfaceDecorator.decorate(context);
        forestSurfaceDecorator.decorate(context);
        grasslandSurfaceDecorator.decorate(context);
        oceanSurfaceDecorator.decorate(context);
        waterDecorator.decorate(context);
        cactusDecorator.decorate(context);
        saplingDecorator.decorate(context);
    }
}