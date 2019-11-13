package net.survival.gen.decoration;

import java.util.Random;

import net.survival.gen.ColumnPrimer;
import net.survival.gen.SurfaceDescription;

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

        random.setSeed(columnPos);

        for (var z = 0; z < ColumnPrimer.ZLENGTH; ++z) {
            for (var x = 0; x < ColumnPrimer.XLENGTH; ++x) {
                decorateStrip(x, z, primer);
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

            var block = layer.getBlock();

            while (thickness > 0) {
                primer.setBlock(x, y, z, block);
                --y;
                --thickness;
            }
        }
    }
}