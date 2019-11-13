package net.survival.gen.terrain;

import net.survival.block.state.StoneBlock;
import net.survival.gen.ColumnPrimer;
import net.survival.util.DoubleMap3D;
import net.survival.util.ImprovedNoiseGenerator;

public class DefaultTerrainGenerator implements TerrainGenerator
{
    private static final int NBLOCK_YLENGTH = ColumnPrimer.YLENGTH / 32;
    private static final int NBLOCK_ZLENGTH = ColumnPrimer.ZLENGTH / 4;
    private static final int NBLOCK_XLENGTH = ColumnPrimer.XLENGTH / 4;
    private static final int NMAP_YLENGTH = (ColumnPrimer.YLENGTH / NBLOCK_YLENGTH) + 1;
    private static final int NMAP_ZLENGTH = (ColumnPrimer.ZLENGTH / NBLOCK_ZLENGTH) + 1;
    private static final int NMAP_XLENGTH = (ColumnPrimer.XLENGTH / NBLOCK_XLENGTH) + 1;

    private static final double MAIN_NOISE_XSCALE = 1.0 / 128.0;
    private static final double MAIN_NOISE_YSCALE = 1.0 / 64.0;
    private static final double MAIN_NOISE_ZSCALE = 1.0 / 128.0;
    private static final int MAIN_NOISE_OCTAVE_COUNT = 6;

    private static final double ELEVATION_BASE = 64.0;
    private static final double ELEVATION_RANGE = 96.0;

    private final long seed;
    private final DoubleMap3D densityMap;

    public DefaultTerrainGenerator(long seed) {
        this.seed = seed;

        densityMap = new DoubleMap3D(NMAP_XLENGTH, NMAP_YLENGTH, NMAP_ZLENGTH);
    }

    @Override
    public void generate(TerrainContext context) {
        var cx = context.getColumnX();
        var cz = context.getColumnZ();
        var offsetX = cx * (NMAP_XLENGTH - 1);
        var offsetZ = cz * (NMAP_ZLENGTH - 1);
        var columnPrimer = context.getColumnPrimer();

        ImprovedNoiseGenerator.generate3D(
                offsetX,
                0.0,
                offsetZ,
                MAIN_NOISE_XSCALE,
                MAIN_NOISE_YSCALE,
                MAIN_NOISE_ZSCALE,
                MAIN_NOISE_OCTAVE_COUNT,
                seed,
                densityMap);

        generateBase(columnPrimer);
    }

    private void generateBase(ColumnPrimer columnPrimer) {
        for (var y = 0; y < ColumnPrimer.YLENGTH; ++y) {
            var noiseMapY = (double) y / NBLOCK_YLENGTH;

            for (var z = 0; z < ColumnPrimer.ZLENGTH; ++z) {
                var noiseMapZ = (double) z / NBLOCK_ZLENGTH;

                for (var x = 0; x < ColumnPrimer.XLENGTH; ++x) {
                    var noiseMapX = (double) x / NBLOCK_XLENGTH;

                    var density = densityMap.sampleLinear(noiseMapX, noiseMapY, noiseMapZ);
                    var threshold = (y - ELEVATION_BASE) / ELEVATION_RANGE;

                    if (density >= threshold) {
                        columnPrimer.setBlock(x, y, z, StoneBlock.INSTANCE);
                    }
                }
            }
        }
    }
}