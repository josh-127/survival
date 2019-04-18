package net.survival.gen.decoration;

import net.survival.blocktype.BlockType;
import net.survival.gen.SurfaceDescription;
import net.survival.gen.SurfaceLayer;

public class DefaultColumnDecorator extends ColumnDecorator
{
    private final int grassId = BlockType.GRASS_BLOCK.getFullId();
    private final int dirtId = BlockType.DIRT.getFullId();

    private final BedrockDecorator bedrockDecorator = new BedrockDecorator();

    private final SurfaceDescription grasslandSurface = new SurfaceDescription.Builder()
            .addLayer(new SurfaceLayer(grassId, 1, 1))
            .addLayer(new SurfaceLayer(dirtId, 3, 5))
            .build();
    private final SurfaceDecorator grasslandSurfaceDecorator = new SurfaceDecorator(grasslandSurface);

    private final WaterDecorator waterDecorator = new WaterDecorator();

    private final boolean includeWater;

    public DefaultColumnDecorator(boolean includeWater) {
        this.includeWater = includeWater;
    }

    @Override
    public void decorate(DecoratorContext context) {
        bedrockDecorator.decorate(context);
        grasslandSurfaceDecorator.decorate(context);

        if (includeWater) {
            waterDecorator.decorate(context);
        }
    }
}