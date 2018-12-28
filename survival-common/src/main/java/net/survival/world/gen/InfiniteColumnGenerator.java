package net.survival.world.gen;

import net.survival.block.BlockType;
import net.survival.util.DoubleMap2D;
import net.survival.util.DoubleMap3D;
import net.survival.util.ImprovedNoiseGenerator3D;
import net.survival.world.biome.BiomeType;
import net.survival.world.column.Column;
import net.survival.world.column.ColumnPos;
import net.survival.world.column.ColumnProvider;
import net.survival.world.gen.layer.GenLayer;
import net.survival.world.gen.layer.GenLayerFactory;

public class InfiniteColumnGenerator implements ColumnProvider
{
    private static final int NBLOCK_YLENGTH = Column.YLENGTH / 32;
    private static final int NBLOCK_ZLENGTH = Column.ZLENGTH / 4;
    private static final int NBLOCK_XLENGTH = Column.XLENGTH / 4;
    private static final int NMAP_YLENGTH = (Column.YLENGTH / NBLOCK_YLENGTH) + 1;
    private static final int NMAP_ZLENGTH = (Column.ZLENGTH / NBLOCK_ZLENGTH) + 1;
    private static final int NMAP_XLENGTH = (Column.XLENGTH / NBLOCK_XLENGTH) + 1;

    private static final double MAIN_NOISE_XSCALE = 1.0 / 128.0;
    private static final double MAIN_NOISE_YSCALE = 1.0 / 128.0;
    private static final double MAIN_NOISE_ZSCALE = 1.0 / 128.0;
    private static final int MAIN_NOISE_OCTAVE_COUNT = 6;

    private static final int BIOME_TRANSITION_XLENGTH = NBLOCK_XLENGTH * 4;
    private static final int BIOME_TRANSITION_ZLENGTH = NBLOCK_ZLENGTH * 4;
    private static final int BIOME_TRANSITION_AREA = BIOME_TRANSITION_XLENGTH
            * BIOME_TRANSITION_ZLENGTH;

    private static final int OCEAN_LEVEL = 64;

    private final ImprovedNoiseGenerator3D mainNoiseGenerator;
    private final GenLayer biomeLayer;

    private final DoubleMap3D densityMap;
    private final DoubleMap2D minElevationMap;
    private final DoubleMap2D elevationRangeMap;

    public InfiniteColumnGenerator(long seed) {
        mainNoiseGenerator = new ImprovedNoiseGenerator3D(MAIN_NOISE_XSCALE, MAIN_NOISE_YSCALE,
                MAIN_NOISE_ZSCALE, MAIN_NOISE_OCTAVE_COUNT, seed);

        biomeLayer = GenLayerFactory.createBiomeLayer(Column.XLENGTH * 4, Column.ZLENGTH * 4, seed);

        densityMap = new DoubleMap3D(NMAP_XLENGTH, NMAP_YLENGTH, NMAP_ZLENGTH);
        minElevationMap = new DoubleMap2D(Column.XLENGTH, Column.ZLENGTH);
        elevationRangeMap = new DoubleMap2D(Column.XLENGTH, Column.ZLENGTH);
    }

    @Override
    public Column provideColumn(long hashedPos) {
        int cx = ColumnPos.columnXFromHashedPos(hashedPos);
        int cz = ColumnPos.columnZFromHashedPos(hashedPos);
        int offsetX = cx * (NMAP_XLENGTH - 1);
        int offsetZ = cz * (NMAP_ZLENGTH - 1);
        int globalX = ColumnPos.toGlobalX(cx, 0);
        int globalZ = ColumnPos.toGlobalZ(cz, 0);

        mainNoiseGenerator.generate(densityMap, offsetX, 0.0, offsetZ);
        biomeLayer.generate(globalX, globalZ);

        Column column = new Column();
        generateBase(column);
        replaceBlocks(cx, cz, column);

        return column;
    }

    private void replaceBlocks(int cx, int cz, Column column) {
        for (int z = 0; z < Column.ZLENGTH; ++z) {
            for (int x = 0; x < Column.XLENGTH; ++x)
                column.setBlock(x, 0, z, BlockType.BEDROCK.id);
        }

        for (int z = 0; z < Column.ZLENGTH; ++z) {
            for (int x = 0; x < Column.XLENGTH; ++x) {
                BiomeType biome = BiomeType.byID(biomeLayer.sampleNearest(x, z));
                int state = 0;
                int counter = 3;

                for (int y = Column.YLENGTH - 1; y >= 1; --y) {
                    if (column.getBlock(x, y, z) != BlockType.TEMP_SOLID.id) {
                        state = 0;
                        counter = 3;
                        continue;
                    }

                    switch (state) {
                    case 0:
                        column.setBlock(x, y, z, biome.getTopBlockID());
                        ++state;
                        break;

                    case 1:
                        if (counter > 0) {
                            column.setBlock(x, y, z, BlockType.DIRT.id);
                            --counter;

                            if (counter == 0) {
                                ++state;
                                counter = 8;
                            }
                        }

                        break;

                    case 2:
                        column.setBlock(x, y, z, BlockType.STONE.id);
                        break;
                    }
                }
            }
        }
    }

    private void generateBase(Column column) {
        generateElevationMaps(minElevationMap, elevationRangeMap, column, biomeLayer);

        for (int y = 0; y < Column.YLENGTH; ++y) {
            double noiseMapY = (double) y / NBLOCK_YLENGTH;

            for (int z = 0; z < Column.ZLENGTH; ++z) {
                double noiseMapZ = (double) z / NBLOCK_ZLENGTH;

                for (int x = 0; x < Column.XLENGTH; ++x) {
                    double noiseMapX = (double) x / NBLOCK_XLENGTH;

                    double density = densityMap.sampleLinear(noiseMapX, noiseMapY, noiseMapZ);

                    double minElevation = minElevationMap.sampleNearest(x, z);
                    double elevationRange = elevationRangeMap.sampleNearest(x, z);
                    double threshold = (y - minElevation) / elevationRange;

                    if (density >= threshold)
                        column.setBlock(x, y, z, BlockType.TEMP_SOLID.id);
                    else if (y <= OCEAN_LEVEL)
                        column.setBlock(x, y, z, BlockType.WATER.id);
                }
            }
        }
    }

    private void generateElevationMaps(DoubleMap2D minElevationMap, DoubleMap2D elevationRangeMap,
            Column column, GenLayer genLayer)
    {
        for (int z = 0; z < Column.ZLENGTH; ++z) {
            for (int x = 0; x < Column.XLENGTH; ++x) {
                double avgMinElevation = 0.0;
                double avgMaxElevation = 0.0;

                for (int subZ = 0; subZ < BIOME_TRANSITION_ZLENGTH; ++subZ) {
                    for (int subX = 0; subX < BIOME_TRANSITION_XLENGTH; ++subX) {
                        BiomeType biome = BiomeType
                                .byID(genLayer.sampleNearest(x + subX, z + subZ));
                        avgMinElevation += biome.getMinElevation();
                        avgMaxElevation += biome.getMaxElevation();
                    }
                }

                avgMinElevation /= BIOME_TRANSITION_AREA;
                avgMaxElevation /= BIOME_TRANSITION_AREA;

                minElevationMap.setPoint(x, z, avgMinElevation);
                elevationRangeMap.setPoint(x, z, avgMaxElevation);
            }
        }
    }
}