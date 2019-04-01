package net.survival.gen;

import java.util.Random;

class SurfaceDecorator extends ColumnDecorator
{
    private final SurfaceDescription description;
    private final Random random = new Random();

    public SurfaceDecorator(SurfaceDescription description) {
        this.description = description;
    }

    @Override
    public void decorate(DecoratorContext context) {
        var columnPos = context.getColumnPos();
        var primer = context.getColumnPrimer();
        var biomeMap = context.getBiomeMap();

        var targetBiome = description.getBiomeType();

        random.setSeed(columnPos);

        for (var z = 0; z < ColumnPrimer.ZLENGTH; ++z) {
            for (var x = 0; x < ColumnPrimer.XLENGTH; ++x) {
                if (biomeMap.sampleNearest(x, z) == targetBiome.ordinal()) {
                    decorateStrip(x, z, primer);
                }
            }
        }
    }

    private void decorateStrip(int x, int z, ColumnPrimer primer) {
        var y = primer.getTopLevel(x, z);
        if (y == -1)
            return;

        var layerCount = description.getLayerCount();

        for (var i = 0; i < layerCount; ++i) {
            var layer = description.getLayer(i);

            var minThickness = layer.getMinThickness();
            var thicknessRange = layer.getThicknessRange();
            var thickness = minThickness + random.nextInt(thicknessRange);
            if (y - thickness < 0) {
                thickness = y;
            }

            var blockId = layer.getBlockId();

            while (thickness > 0) {
                primer.setBlockFullId(x, y, z, blockId);
                --y;
                --thickness;
            }
        }
    }
}