package net.survival.world.gen.terrain;

import net.survival.block.BlockType;
import net.survival.util.DoubleMap2D;
import net.survival.util.DoubleMap3D;
import net.survival.util.ImprovedNoiseGenerator3D;
import net.survival.world.chunk.Chunk;
import net.survival.world.chunk.ChunkPos;
import net.survival.world.gen.BiomeType;
import net.survival.world.gen.layer.GenLayer;
import net.survival.world.gen.layer.GenLayerFactory;

class TerrainGenerator
{
    private static final int NBLOCK_YLENGTH = Chunk.YLENGTH / 32;
    private static final int NBLOCK_ZLENGTH = Chunk.ZLENGTH / 4;
    private static final int NBLOCK_XLENGTH = Chunk.XLENGTH / 4;
    private static final int NMAP_YLENGTH = (Chunk.YLENGTH / NBLOCK_YLENGTH) + 1;
    private static final int NMAP_ZLENGTH = (Chunk.ZLENGTH / NBLOCK_ZLENGTH) + 1;
    private static final int NMAP_XLENGTH = (Chunk.XLENGTH / NBLOCK_XLENGTH) + 1;
    
    private static final double MAIN_NOISE_XSCALE = 1.0 / 128.0;
    private static final double MAIN_NOISE_YSCALE = 1.0 / 128.0;
    private static final double MAIN_NOISE_ZSCALE = 1.0 / 128.0;
    private static final int MAIN_NOISE_OCTAVE_COUNT = 6;
    
    private static final int BIOME_TRANSITION_XLENGTH = NBLOCK_XLENGTH * 4;
    private static final int BIOME_TRANSITION_ZLENGTH = NBLOCK_ZLENGTH * 4;
    private static final int BIOME_TRANSITION_AREA = BIOME_TRANSITION_XLENGTH * BIOME_TRANSITION_ZLENGTH;
    
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

        biomeLayer = GenLayerFactory.createBiomeLayer(Chunk.XLENGTH * 4, Chunk.ZLENGTH * 4, seed);
        stoneLayers = new GenLayer[BlockType.getStoneTypes().length];
        
        for (int i = 0; i < stoneLayers.length; ++i)
            stoneLayers[i] = GenLayerFactory.createStoneLayer(Chunk.XLENGTH * 4, Chunk.ZLENGTH * 4, seed + i + 1L);
        
        densityMap = new DoubleMap3D(NMAP_XLENGTH, NMAP_YLENGTH, NMAP_ZLENGTH);
        minElevationMap = new DoubleMap2D(Chunk.XLENGTH, Chunk.ZLENGTH);
        elevationRangeMap = new DoubleMap2D(Chunk.XLENGTH, Chunk.ZLENGTH);
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
        for (int z = 0; z < Chunk.ZLENGTH; ++z) {
            for (int x = 0; x < Chunk.XLENGTH; ++x) {
                BiomeType biome = BiomeType.byID(biomeLayer.sampleNearest(x, z));
                short stoneBlockID = BlockType.getStoneTypes()[stoneLayers[0].sampleNearest(x, z)].getID();
                int state = 0;
                int counter = 3;
                
                for (int y = Chunk.YLENGTH - 1; y >= 0; --y) {
                    if (chunkPrimer.getBlockID(x, y, z) != BlockType.TEMP_SOLID.getID()) {
                        state = 0;
                        counter = 3;
                        continue;
                    }
                    
                    switch (state) {
                    case 0:
                        chunkPrimer.setBlockID(x, y, z, biome.getTopBlockID());
                        ++state;
                        break;
                        
                    case 1:
                        if (counter > 0) {
                            chunkPrimer.setBlockID(x, y, z, BlockType.GRANITE_DIRT.getID());
                            --counter;
                            
                            if (counter == 0) {
                                ++state;
                                counter = 8;
                            }
                        }
                        
                        break;
                        
                    case 2:
                        chunkPrimer.setBlockID(x, y, z, stoneBlockID);
                        break;
                    }
                }
            }
        }
    }
    
    private void generateBase(ChunkPrimer chunkPrimer) {
        generateElevationMaps(minElevationMap, elevationRangeMap, chunkPrimer, biomeLayer);
        
        for (int y = 0; y < Chunk.YLENGTH; ++y) {
            double noiseMapY = (double) y / NBLOCK_YLENGTH;
            
            for (int z = 0; z < Chunk.ZLENGTH; ++z) {
                double noiseMapZ = (double) z / NBLOCK_ZLENGTH;
                
                for (int x = 0; x < Chunk.XLENGTH; ++x) {
                    double noiseMapX = (double) x / NBLOCK_XLENGTH;
                    
                    double density = densityMap.sampleLinear(noiseMapX, noiseMapY, noiseMapZ);
                    
                    double minElevation = minElevationMap.sampleNearest(x, z);
                    double elevationRange = elevationRangeMap.sampleNearest(x, z);
                    double threshold = (y - minElevation) / elevationRange;
                    
                    if (density >= threshold)
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
        for (int z = 0; z < Chunk.ZLENGTH; ++z) {
            for (int x = 0; x < Chunk.XLENGTH; ++x) {
                double avgMinElevation = 0.0;
                double avgMaxElevation = 0.0;
                
                for (int subZ = 0; subZ < BIOME_TRANSITION_ZLENGTH; ++subZ) {
                    for (int subX = 0; subX < BIOME_TRANSITION_XLENGTH; ++subX) {
                        BiomeType biome = BiomeType.byID(genLayer.sampleNearest(x + subX, z + subZ));
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