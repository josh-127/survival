package net.survival.util;

import static net.survival.util.ImprovedNoiseMapHelper.fade;
import static net.survival.util.ImprovedNoiseMapHelper.generatePermutations;
import static net.survival.util.MathEx.lerp;

public class ImprovedNoiseGenerator3D
{
    public final double scaleX;
    public final double scaleY;
    public final double scaleZ;
    public final int octaveCount;
    public final long seed;
    
    private final int[] permutations;
    
    public ImprovedNoiseGenerator3D(double scaleX, double scaleY, double scaleZ, int octaveCount, long seed) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.scaleZ = scaleZ;
        this.octaveCount = octaveCount;
        this.seed = seed;
        
        permutations = generatePermutations(seed);
    }
    
    public void generate(DoubleMap3D map, double offsetX, double offsetY, double offsetZ) {
        for (int y = 0; y < map.lengthY; ++y) {
            for (int z = 0; z < map.lengthZ; ++z) {
                for (int x = 0; x < map.lengthX; ++x)
                    map.setPoint(x, y, z, 0.0);
            }
        }
        
        double octaveScale = 1.0;
        
        for (int o = 0; o < octaveCount; ++o) {
            double octaveScaleX = scaleX * octaveScale;
            double octaveScaleY = scaleY * octaveScale;
            double octaveScaleZ = scaleZ * octaveScale;
            double invOctaveScale = 1.0 / octaveScale;
            
            for (int y = 0; y < map.lengthY; ++y) {
                double noisePosY = (offsetY + y) * octaveScaleY;
                
                for (int z = 0; z < map.lengthZ; ++z) {
                    double noisePosZ = (offsetZ + z) * octaveScaleZ;
                    
                    for (int x = 0; x < map.lengthX; ++x) {
                        double noisePosX = (offsetX + x) * octaveScaleX;
                        double prevOctave = map.sampleNearest(x, y, z);
                        double currentOctave = valueAt(noisePosX, noisePosY, noisePosZ) * invOctaveScale;
                        
                        map.setPoint(x, y, z, prevOctave + currentOctave);
                    }
                }
            }
            
            octaveScale *= 2.0; 
        }
        
        if (octaveCount > 1) {
            double clamper = 1.0 / (2.0 - 1.0 / octaveScale);
            for (int y = 0; y < map.lengthY; ++y) {
                for (int z = 0; z < map.lengthZ; ++z) {
                    for (int x = 0; x < map.lengthX; ++x)
                        map.setPoint(x, y, z, map.sampleNearest(x, y, z) * clamper);
                }
            }
        }
    }
    
    private double valueAt(double x, double y, double z) {
        double floorX = Math.floor(x);
        double floorY = Math.floor(y);
        double floorZ = Math.floor(z);
        int indexX = ((int) floorX) & 255;
        int indexY = ((int) floorY) & 255;
        int indexZ = ((int) floorZ) & 255;
        
        int permY     = permutations[indexY];
        int permY_Z   = permutations[permY + indexZ];
        int permY_NZ  = permutations[permY + indexZ + 1];
        int permNY    = permutations[indexY + 1];
        int permNY_Z  = permutations[permNY + indexZ];
        int permNY_NZ = permutations[permNY + indexZ + 1];
        
        int hashBBL = permutations[permY_Z   + indexX    ];
        int hashBBR = permutations[permY_Z   + indexX + 1];
        int hashBFL = permutations[permY_NZ  + indexX    ];
        int hashBFR = permutations[permY_NZ  + indexX + 1];
        int hashTBL = permutations[permNY_Z  + indexX    ];
        int hashTBR = permutations[permNY_Z  + indexX + 1];
        int hashTFL = permutations[permNY_NZ + indexX    ];
        int hashTFR = permutations[permNY_NZ + indexX + 1];

        double fracX = x - floorX;
        double fracY = y - floorY;
        double fracZ = z - floorZ;
        
        double dotBBL = dotProductOfGradientAndDistance(hashBBL, fracX,       fracY,       fracZ      );
        double dotBBR = dotProductOfGradientAndDistance(hashBBR, fracX - 1.0, fracY,       fracZ      );
        double dotBFL = dotProductOfGradientAndDistance(hashBFL, fracX,       fracY,       fracZ - 1.0);
        double dotBFR = dotProductOfGradientAndDistance(hashBFR, fracX - 1.0, fracY,       fracZ - 1.0);
        double dotTBL = dotProductOfGradientAndDistance(hashTBL, fracX,       fracY - 1.0, fracZ      );
        double dotTBR = dotProductOfGradientAndDistance(hashTBR, fracX - 1.0, fracY - 1.0, fracZ      );
        double dotTFL = dotProductOfGradientAndDistance(hashTFL, fracX,       fracY - 1.0, fracZ - 1.0);
        double dotTFR = dotProductOfGradientAndDistance(hashTFR, fracX - 1.0, fracY - 1.0, fracZ - 1.0);
        
        double fadeX = fade(fracX);
        double fadeY = fade(fracY);
        double fadeZ = fade(fracZ);
        
        return lerp(lerp(lerp(dotBBL, dotBBR, fadeX),
                         lerp(dotBFL, dotBFR, fadeX),
                         fadeZ),
                    lerp(lerp(dotTBL, dotTBR, fadeX),
                         lerp(dotTFL, dotTFR, fadeX),
                         fadeZ),
                    fadeY);
    }
    
    private static double dotProductOfGradientAndDistance(int hash, double x, double y, double z) {
        switch (hash & 15) {
        case 0:  return +x +y   ; // < 1,  1,  0>
        case 1:  return -x +y   ; // <-1,  1,  0>
        case 2:  return +x -y   ; // < 1, -1,  0>
        case 3:  return -x -y   ; // <-1, -1,  0>
        case 4:  return +x    +z; // < 1,  0,  1>
        case 5:  return -x    +z; // <-1,  0,  1>
        case 6:  return +x    -z; // < 1,  0, -1>
        case 7:  return -x    -z; // <-1,  0, -1>
        case 8:  return    +y +z; // < 0,  1,  1>
        case 9:  return    -y +z; // < 0, -1,  1>
        case 10: return    +y -z; // < 0,  1, -1>
        case 11: return    -y -z; // < 0, -1, -1>
        case 12: return +x +y   ; // < 1,  1,  0>
        case 13: return    -y +z; // < 0, -1,  1>
        case 14: return -x +y   ; // <-1,  1,  0>
        case 15: return    -y -z; // < 0, -1, -1>
        default: return 0.0;
        }
    }
}