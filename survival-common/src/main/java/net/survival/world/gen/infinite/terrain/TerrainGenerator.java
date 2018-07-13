package net.survival.world.gen.infinite.terrain;

import net.survival.block.BlockType;
import net.survival.util.DoubleMap2D;
import net.survival.util.DoubleMap3D;
import net.survival.util.ImprovedNoiseGenerator3D;
import net.survival.world.chunk.Chunk;
import net.survival.world.chunk.ChunkPos;
import net.survival.world.gen.infinite.BiomeType;
import net.survival.world.gen.infinite.layer.GenLayer;
import net.survival.world.gen.infinite.layer.GenLayerFactory;

class TerrainGenerator
{
    private static final int NBLOCK_YLENGTH = Chunk.YLENGTH / 32;
    private static final int NBLOCK_ZLENGTH = Chunk.ZLENGTH / 4;
    private static final int NBLOCK_XLENGTH = Chunk.XLENGTH / 4;
    private static final int NMAP_YLENGTH = (Chunk.YLENGTH / NBLOCK_YLENGTH) + 1;
    private static final int NMAP_ZLENGTH = (Chunk.ZLENGTH / NBLOCK_ZLENGTH) + 1;
    private static final int NMAP_XLENGTH = (Chunk.XLENGTH / NBLOCK_XLENGTH) + 1;
    
    private static final double MAIN_NOISE_XSCALE = 1.0 / 128.0;
    private static final double MAIN_NOISE_YSCALE = 1.0 / 96.0;
    private static final double MAIN_NOISE_ZSCALE = 1.0 / 128.0;
    private static final int MAIN_NOISE_OCTAVE_COUNT = 8;
    
    private static final int BIOME_TRANSITION_XLENGTH = NBLOCK_XLENGTH * 8;
    private static final int BIOME_TRANSITION_ZLENGTH = NBLOCK_ZLENGTH * 8;
    
    private static final int OCEAN_LEVEL = 64;
    
    private final ImprovedNoiseGenerator3D mainNoiseGenerator;
    private final GenLayer biomeLayer;
    private final GenLayer[] stoneLayers;
    
    private final DoubleMap3D densityMap;
    private final DoubleMap2D minElevationMap;
    private final DoubleMap2D elevationRangeMap;
    
    public TerrainGenerator(long seed) {
        mainNoiseGenerator = new ImprovedNoiseGenerator3D(MAIN_NOISE_XSCALE, MAIN_NOISE_YSCALE, MAIN_NOISE_ZSCALE,
                MAIN_NOISE_OCTAVE_COUNT, seed);

        biomeLayer = GenLayerFactory.createBiomeLayer(64, 64, seed);
        stoneLayers = new GenLayer[BlockType.getStoneTypes().length];
        
        for (int i = 0; i < stoneLayers.length; ++i)
            stoneLayers[i] = GenLayerFactory.createStoneLayer(64, 64, seed + i + 1L);
        
        densityMap = new DoubleMap3D(NMAP_XLENGTH, NMAP_YLENGTH, NMAP_ZLENGTH);
        minElevationMap = new DoubleMap2D(NMAP_XLENGTH, NMAP_ZLENGTH);
        elevationRangeMap = new DoubleMap2D(NMAP_XLENGTH, NMAP_ZLENGTH);
    }
    
    public ChunkPrimer generate(int cx, int cz) {
        int offsetX = cx * (NMAP_XLENGTH - 1);
        int offsetZ = cz * (NMAP_ZLENGTH - 1);
        int globalX = ChunkPos.toGlobalX(cx, 0);
        int globalZ = ChunkPos.toGlobalZ(cz, 0);
        
        mainNoiseGenerator.generate(densityMap, offsetX, 0.0, offsetZ);
        biomeLayer.generate(globalX, globalZ);
        for (int i = 0; i < stoneLayers.length; ++i)
            stoneLayers[i].generate(globalX, globalZ);
        
        ChunkPrimer chunkPrimer = new ChunkPrimer(cx, cz);
        generateBase(chunkPrimer);
        replaceBlocks(chunkPrimer);
        
        return chunkPrimer;
    }
    
    private void replaceBlocks(ChunkPrimer chunkPrimer) {
        for (int y = 0; y < Chunk.YLENGTH - 1; ++y) {
            for (int z = 0; z < Chunk.ZLENGTH; ++z) {
                for (int x = 0; x < Chunk.XLENGTH; ++x) {
                    BiomeType biome = BiomeType.byID(biomeLayer.sampleNearest(x, z));
                    short stoneBlockID = BlockType.getStoneTypes()[stoneLayers[0].sampleNearest(x, z)].getID();

                    if (chunkPrimer.getBlockID(x, y, z) == BlockType.TEMP_SOLID.getID()) {
                        if (chunkPrimer.getBlockID(x, y + 1, z) == BlockType.EMPTY.getID())
                            chunkPrimer.setBlockID(x, y, z, biome.getTopBlockID());
                        else
                            chunkPrimer.setBlockID(x, y, z, stoneBlockID);
                    }
                }
            }
        }
    }
    
    private void generateBase(ChunkPrimer chunkPrimer) {
        generateElevationMaps(minElevationMap, elevationRangeMap, chunkPrimer, biomeLayer);
        generateDensityMap(densityMap, chunkPrimer.chunkX, chunkPrimer.chunkZ, chunkPrimer, minElevationMap,
                elevationRangeMap);
        
        for (int y = 0; y < Chunk.YLENGTH; ++y) {
            double noiseMapY = (double) y / NBLOCK_YLENGTH;
            
            for (int z = 0; z < Chunk.ZLENGTH; ++z) {
                double noiseMapZ = (double) z / NBLOCK_ZLENGTH;
                
                for (int x = 0; x < Chunk.XLENGTH; ++x) {
                    double noiseMapX = (double) x / NBLOCK_XLENGTH;
                    
                    if (densityMap.sampleLinear(noiseMapX, noiseMapY, noiseMapZ) >= 0)
                        chunkPrimer.setBlockID(x, y, z, BlockType.TEMP_SOLID.getID());
                    else if (y <= OCEAN_LEVEL)
                        chunkPrimer.setBlockID(x, y, z, BlockType.WATER.getID());
                }
            }
        }
    }

    private void generateElevationMaps(DoubleMap2D minElevationMap, DoubleMap2D elevationRangeMap,
            ChunkPrimer chunkPrimer, GenLayer genLayer)
    {
        final int NBLOCK_BASE_AREA = BIOME_TRANSITION_XLENGTH * BIOME_TRANSITION_ZLENGTH;
        
        for (int blockZ = 0; blockZ < NMAP_ZLENGTH; ++blockZ) {
            for (int blockX = 0; blockX < NMAP_XLENGTH; ++blockX) {
                double avgMinElevation = 0.0;
                double avgMaxElevation = 0.0;
                
                for (int subZ = 0; subZ < BIOME_TRANSITION_ZLENGTH; ++subZ) {
                    for (int subX = 0; subX < BIOME_TRANSITION_XLENGTH; ++subX) {
                        int x = subX + blockX * NBLOCK_XLENGTH;
                        int z = subZ + blockZ * NBLOCK_ZLENGTH;
                        BiomeType biome = BiomeType.byID(genLayer.sampleNearest(x, z));
                        
                        avgMinElevation += biome.getMinElevation();
                        avgMaxElevation += biome.getMaxElevation();
                    }
                }
                
                avgMinElevation /= NBLOCK_BASE_AREA;
                avgMaxElevation /= NBLOCK_BASE_AREA;

                minElevationMap.setPoint(blockX, blockZ, avgMinElevation);
                elevationRangeMap.setPoint(blockX, blockZ, avgMaxElevation - avgMinElevation);
            }
        }
    }

    private void generateDensityMap(DoubleMap3D map, int cx, int cz, ChunkPrimer chunkPrimer,
            DoubleMap2D minElevationMap, DoubleMap2D elevationRangeMap)
    {
        for (int y = 0; y < map.lengthY; ++y) {
            int globalY = y * NBLOCK_YLENGTH;

            for (int z = 0; z < map.lengthZ; ++z) {
                for (int x = 0; x < map.lengthX; ++x) {
                    double density = map.sampleNearest(x, y, z);
                    double elevationRange = elevationRangeMap.sampleNearest(x, z);
                    double minElevation = minElevationMap.sampleNearest(x, z);
                    density -= (globalY - minElevation) / elevationRange;
                    map.setPoint(x, y, z, density);
                }
            }
        }
    }
}