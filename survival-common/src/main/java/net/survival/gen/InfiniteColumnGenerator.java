package net.survival.gen;

import net.survival.block.column.Column;
import net.survival.block.column.ColumnPos;
import net.survival.block.column.ColumnProvider;
import net.survival.blocktype.BlockType;
import net.survival.gen.biome.BiomeType;
import net.survival.gen.layer.GenLayer;
import net.survival.gen.layer.GenLayerFactory;
import net.survival.util.DoubleMap2D;
import net.survival.util.DoubleMap3D;
import net.survival.util.ImprovedNoiseGenerator3D;

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

    private final int bedrockID;
    private final int tempSolidID;
    private final int stoneID;
    private final int dirtID;
    private final int waterID;

    public InfiniteColumnGenerator(long seed) {
        mainNoiseGenerator = new ImprovedNoiseGenerator3D(MAIN_NOISE_XSCALE, MAIN_NOISE_YSCALE,
                MAIN_NOISE_ZSCALE, MAIN_NOISE_OCTAVE_COUNT, seed);

        biomeLayer = GenLayerFactory.createBiomeLayer(Column.XLENGTH * 4, Column.ZLENGTH * 4, seed);

        densityMap = new DoubleMap3D(NMAP_XLENGTH, NMAP_YLENGTH, NMAP_ZLENGTH);
        minElevationMap = new DoubleMap2D(Column.XLENGTH, Column.ZLENGTH);
        elevationRangeMap = new DoubleMap2D(Column.XLENGTH, Column.ZLENGTH);

        bedrockID = BlockType.BEDROCK.getFullID();
        tempSolidID = BlockType.TEMP_SOLID.getFullID();
        stoneID = BlockType.STONE.getFullID();
        dirtID = BlockType.DIRT.getFullID();
        waterID = BlockType.WATER.getFullID();
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
                column.setBlockFullID(x, 0, z, bedrockID);
        }

        for (int z = 0; z < Column.ZLENGTH; ++z) {
            for (int x = 0; x < Column.XLENGTH; ++x) {
                BiomeType biome = BiomeType.byID(biomeLayer.sampleNearest(x, z));
                int state = 0;
                int counter = 3;

                for (int y = Column.YLENGTH - 1; y >= 1; --y) {
                    if (column.getBlockFullID(x, y, z) != tempSolidID) {
                        state = 0;
                        counter = 3;
                        continue;
                    }

                    switch (state) {
                    case 0:
                        column.setBlockFullID(x, y, z, biome.getTopBlockID());
                        ++state;
                        break;

                    case 1:
                        if (counter > 0) {
                            column.setBlockFullID(x, y, z, dirtID);
                            --counter;

                            if (counter == 0) {
                                ++state;
                                counter = 8;
                            }
                        }

                        break;

                    case 2:
                        column.setBlockFullID(x, y, z, stoneID);
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
                        column.setBlockFullID(x, y, z, tempSolidID);
                    else if (y <= OCEAN_LEVEL)
                        column.setBlockFullID(x, y, z, waterID);
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