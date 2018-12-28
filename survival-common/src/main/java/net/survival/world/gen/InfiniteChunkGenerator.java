package net.survival.world.gen;

import net.survival.block.BlockType;
import net.survival.util.DoubleMap2D;
import net.survival.util.DoubleMap3D;
import net.survival.util.ImprovedNoiseGenerator3D;
import net.survival.world.biome.BiomeType;
import net.survival.world.chunk.ChunkColumn;
import net.survival.world.chunk.ChunkProvider;
import net.survival.world.chunk.ChunkColumnPos;
import net.survival.world.gen.layer.GenLayer;
import net.survival.world.gen.layer.GenLayerFactory;

public class InfiniteChunkGenerator implements ChunkProvider
{
    private static final int NBLOCK_YLENGTH = ChunkColumn.YLENGTH / 32;
    private static final int NBLOCK_ZLENGTH = ChunkColumn.ZLENGTH / 4;
    private static final int NBLOCK_XLENGTH = ChunkColumn.XLENGTH / 4;
    private static final int NMAP_YLENGTH = (ChunkColumn.YLENGTH / NBLOCK_YLENGTH) + 1;
    private static final int NMAP_ZLENGTH = (ChunkColumn.ZLENGTH / NBLOCK_ZLENGTH) + 1;
    private static final int NMAP_XLENGTH = (ChunkColumn.XLENGTH / NBLOCK_XLENGTH) + 1;

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

    public InfiniteChunkGenerator(long seed) {
        mainNoiseGenerator = new ImprovedNoiseGenerator3D(MAIN_NOISE_XSCALE, MAIN_NOISE_YSCALE,
                MAIN_NOISE_ZSCALE, MAIN_NOISE_OCTAVE_COUNT, seed);

        biomeLayer = GenLayerFactory.createBiomeLayer(ChunkColumn.XLENGTH * 4, ChunkColumn.ZLENGTH * 4, seed);

        densityMap = new DoubleMap3D(NMAP_XLENGTH, NMAP_YLENGTH, NMAP_ZLENGTH);
        minElevationMap = new DoubleMap2D(ChunkColumn.XLENGTH, ChunkColumn.ZLENGTH);
        elevationRangeMap = new DoubleMap2D(ChunkColumn.XLENGTH, ChunkColumn.ZLENGTH);
    }

    @Override
    public ChunkColumn provideChunk(long hashedPos) {
        int cx = ChunkColumnPos.chunkXFromHashedPos(hashedPos);
        int cz = ChunkColumnPos.chunkZFromHashedPos(hashedPos);
        int offsetX = cx * (NMAP_XLENGTH - 1);
        int offsetZ = cz * (NMAP_ZLENGTH - 1);
        int globalX = ChunkColumnPos.toGlobalX(cx, 0);
        int globalZ = ChunkColumnPos.toGlobalZ(cz, 0);

        mainNoiseGenerator.generate(densityMap, offsetX, 0.0, offsetZ);
        biomeLayer.generate(globalX, globalZ);

        ChunkColumn chunkColumn = new ChunkColumn();
        generateBase(chunkColumn);
        replaceBlocks(cx, cz, chunkColumn);

        return chunkColumn;
    }

    private void replaceBlocks(int cx, int cz, ChunkColumn chunkColumn) {
        for (int z = 0; z < ChunkColumn.ZLENGTH; ++z) {
            for (int x = 0; x < ChunkColumn.XLENGTH; ++x)
                chunkColumn.setBlock(x, 0, z, BlockType.BEDROCK.id);
        }

        for (int z = 0; z < ChunkColumn.ZLENGTH; ++z) {
            for (int x = 0; x < ChunkColumn.XLENGTH; ++x) {
                BiomeType biome = BiomeType.byID(biomeLayer.sampleNearest(x, z));
                int state = 0;
                int counter = 3;

                for (int y = ChunkColumn.YLENGTH - 1; y >= 1; --y) {
                    if (chunkColumn.getBlock(x, y, z) != BlockType.TEMP_SOLID.id) {
                        state = 0;
                        counter = 3;
                        continue;
                    }

                    switch (state) {
                    case 0:
                        chunkColumn.setBlock(x, y, z, biome.getTopBlockID());
                        ++state;
                        break;

                    case 1:
                        if (counter > 0) {
                            chunkColumn.setBlock(x, y, z, BlockType.DIRT.id);
                            --counter;

                            if (counter == 0) {
                                ++state;
                                counter = 8;
                            }
                        }

                        break;

                    case 2:
                        chunkColumn.setBlock(x, y, z, BlockType.STONE.id);
                        break;
                    }
                }
            }
        }
    }

    private void generateBase(ChunkColumn chunkColumn) {
        generateElevationMaps(minElevationMap, elevationRangeMap, chunkColumn, biomeLayer);

        for (int y = 0; y < ChunkColumn.YLENGTH; ++y) {
            double noiseMapY = (double) y / NBLOCK_YLENGTH;

            for (int z = 0; z < ChunkColumn.ZLENGTH; ++z) {
                double noiseMapZ = (double) z / NBLOCK_ZLENGTH;

                for (int x = 0; x < ChunkColumn.XLENGTH; ++x) {
                    double noiseMapX = (double) x / NBLOCK_XLENGTH;

                    double density = densityMap.sampleLinear(noiseMapX, noiseMapY, noiseMapZ);

                    double minElevation = minElevationMap.sampleNearest(x, z);
                    double elevationRange = elevationRangeMap.sampleNearest(x, z);
                    double threshold = (y - minElevation) / elevationRange;

                    if (density >= threshold)
                        chunkColumn.setBlock(x, y, z, BlockType.TEMP_SOLID.id);
                    else if (y <= OCEAN_LEVEL)
                        chunkColumn.setBlock(x, y, z, BlockType.WATER.id);
                }
            }
        }
    }

    private void generateElevationMaps(DoubleMap2D minElevationMap, DoubleMap2D elevationRangeMap,
            ChunkColumn chunkColumn, GenLayer genLayer)
    {
        for (int z = 0; z < ChunkColumn.ZLENGTH; ++z) {
            for (int x = 0; x < ChunkColumn.XLENGTH; ++x) {
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