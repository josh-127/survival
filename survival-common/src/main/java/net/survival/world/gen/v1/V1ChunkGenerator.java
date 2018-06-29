package net.survival.world.gen.v1;

import net.survival.block.BlockType;
import net.survival.util.DoubleMap2D;
import net.survival.util.DoubleMap3D;
import net.survival.util.ImprovedNoiseGenerator3D;
import net.survival.world.chunk.Chunk;
import net.survival.world.chunk.ChunkGenerator;
import net.survival.world.gen.v1.biome.BiomeType;
import net.survival.world.gen.v1.layer.GenLayer;
import net.survival.world.gen.v1.layer.GenLayerFactory;

public class V1ChunkGenerator implements ChunkGenerator
{
    private final int NBLOCK_YLENGTH = Chunk.YLENGTH / 4;
    private final int NBLOCK_ZLENGTH = Chunk.ZLENGTH / 8;
    private final int NBLOCK_XLENGTH = Chunk.XLENGTH / 8;
    private final int NMAP_YLENGTH = (Chunk.YLENGTH / NBLOCK_YLENGTH) + 2;
    private final int NMAP_ZLENGTH = (Chunk.ZLENGTH / NBLOCK_ZLENGTH) + 1;
    private final int NMAP_XLENGTH = (Chunk.XLENGTH / NBLOCK_XLENGTH) + 1;
    
    private final double MAIN_NOISE_XSCALE = 1.0 / 128.0;
    private final double MAIN_NOISE_YSCALE = 1.0 / 96.0;
    private final double MAIN_NOISE_ZSCALE = 1.0 / 128.0;
    private final int MAIN_NOISE_OCTAVE_COUNT = 8;
    
    private final int BIOME_TRANSITION_XLENGTH = NBLOCK_XLENGTH * 4;
    private final int BIOME_TRANSITION_ZLENGTH = NBLOCK_ZLENGTH * 4;

    private final ImprovedNoiseGenerator3D mainNoiseGenerator;
    private final GenLayer biomeLayer;
    private final GenLayer[] stoneLayers;
    
    private final DoubleMap3D densityMap;
    private final DoubleMap2D minElevationMap;
    private final DoubleMap2D elevationRangeMap;
    
    private int prevChunkX;
    private int prevChunkZ;
    
    public V1ChunkGenerator(long seed) {
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

    @Override
    public void generateTerrain(Chunk chunk) {
        int offsetX = chunk.chunkX * (NMAP_XLENGTH - 1);
        int offsetY = chunk.chunkY * (NMAP_YLENGTH - 2);
        int offsetZ = chunk.chunkZ * (NMAP_ZLENGTH - 1);
        int globalX = chunk.toGlobalX(0);
        int globalZ = chunk.toGlobalZ(0);
        
        mainNoiseGenerator.generate(densityMap, offsetX, offsetY, offsetZ);
        
        if (chunk.chunkX != prevChunkX || chunk.chunkZ != prevChunkZ) {
            biomeLayer.generate(globalX, globalZ);
            for (int i = 0; i < stoneLayers.length; ++i)
                stoneLayers[i].generate(globalX, globalZ);
        }
        
        generateBase(chunk);
        
        prevChunkX = chunk.chunkX;
        prevChunkZ = chunk.chunkZ;
    }

    @Override
    public void populate(Chunk chunk) {
    }
    
    private void generateBase(Chunk chunk) {
        generateElevationMaps(minElevationMap, elevationRangeMap, chunk, biomeLayer);
        generateDensityMap(densityMap, chunk, minElevationMap, elevationRangeMap);
        
        for (int y = 0; y < Chunk.YLENGTH; ++y) {
            double noiseMapY      = (double) y / NBLOCK_YLENGTH;
            double noiseMapYAbove = (double) (y + 1) / NBLOCK_YLENGTH;
            
            for (int z = 0; z < Chunk.ZLENGTH; ++z) {
                double noiseMapZ = (double) z / NBLOCK_ZLENGTH;
                
                for (int x = 0; x < Chunk.XLENGTH; ++x) {
                    double noiseMapX = (double) x / NBLOCK_XLENGTH;
                    
                    BiomeType biome = BiomeType.byID(biomeLayer.sampleNearest(x, z));
                    short stoneBlockID = BlockType.getStoneTypes()[stoneLayers[0].sampleNearest(x, z)].getID();
                    
                    if (densityMap.sampleLinear(noiseMapX, noiseMapY, noiseMapZ) >= 0) {
                        if (densityMap.sampleLinear(noiseMapX, noiseMapYAbove, noiseMapZ) < 0)
                            chunk.setBlockID(x, y, z, biome.getTopBlockID());
                        else
                            chunk.setBlockID(x, y, z, stoneBlockID);
                    }
                    else if (chunk.toGlobalY(y) <= 0) {
                        chunk.setBlockID(x, y, z, BlockType.WATER.getID());
                    }
                }
            }
        }
    }
    
    private void generateElevationMaps(DoubleMap2D minElevationMap, DoubleMap2D elevationRangeMap, Chunk chunk,
            GenLayer genLayer)
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

    private void generateDensityMap(DoubleMap3D map, Chunk chunk, DoubleMap2D minElevationMap,
            DoubleMap2D elevationRangeMap)
    {
        for (int y = 0; y < map.lengthY; ++y) {
            int globalY = (y + (chunk.chunkY * (NMAP_YLENGTH - 2))) * NBLOCK_YLENGTH;

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