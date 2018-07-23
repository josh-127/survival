package net.survival.util;

import static net.survival.util.ImprovedNoiseMapHelper.fade;
import static net.survival.util.ImprovedNoiseMapHelper.generatePermutations;
import static net.survival.util.MathEx.lerp;

public class ImprovedNoiseGenerator2D
{
    public final double scaleX;
    public final double scaleZ;
    public final int octaveCount;
    public final long seed;

    private final int[] permutations;

    public ImprovedNoiseGenerator2D(double scaleX, double scaleZ, int octaveCount, long seed) {
        this.scaleX = scaleX;
        this.scaleZ = scaleZ;
        this.octaveCount = octaveCount;
        this.seed = seed;

        permutations = generatePermutations(seed);
    }

    public void generate(DoubleMap2D map, double offsetX, double offsetZ) {
        for (int z = 0; z < map.lengthZ; ++z) {
            for (int x = 0; x < map.lengthX; ++x)
                map.setPoint(x, z, 0.0);
        }

        double octaveScale = 1.0;

        for (int o = 0; o < octaveCount; ++o) {
            double octaveScaleX = scaleX * octaveScale;
            double octaveScaleZ = scaleZ * octaveScale;
            double invOctaveScale = 1.0 / octaveScale;

            for (int z = 0; z < map.lengthZ; ++z) {
                double noisePosZ = (offsetZ + z) * octaveScaleZ;

                for (int x = 0; x < map.lengthX; ++x) {
                    double noisePosX = (offsetX + x) * octaveScaleX;
                    double prevOctave = map.sampleNearest(x, z);
                    double currentOctave = valueAt(noisePosX, noisePosZ) * invOctaveScale;

                    map.setPoint(x, z, prevOctave + currentOctave);
                }
            }

            octaveScale *= 2.0;
        }

        if (octaveCount > 1) {
            double clamper = 1.0 / (2.0 - 1.0 / octaveScale);
            for (int z = 0; z < map.lengthZ; ++z) {
                for (int x = 0; x < map.lengthX; ++x)
                    map.setPoint(x, z, map.sampleNearest(x, z) * clamper);
            }
        }
    }

    private double valueAt(double x, double z) {
        double floorX = Math.floor(x);
        double floorZ = Math.floor(z);
        int indexX = ((int) floorX) & 255;
        int indexZ = ((int) floorZ) & 255;

        int hashTL = permutations[permutations[indexZ] + indexX];
        int hashTR = permutations[permutations[indexZ] + indexX + 1];
        int hashBL = permutations[permutations[indexZ + 1] + indexX];
        int hashBR = permutations[permutations[indexZ + 1] + indexX + 1];

        double fracX = x - floorX;
        double fracZ = z - floorZ;

        double dotTL = dotProductOfGradientAndDistance(hashTL, fracX, fracZ);
        double dotTR = dotProductOfGradientAndDistance(hashTR, fracX - 1.0, fracZ);
        double dotBL = dotProductOfGradientAndDistance(hashBL, fracX, fracZ - 1.0);
        double dotBR = dotProductOfGradientAndDistance(hashBR, fracX - 1.0, fracZ - 1.0);

        double fadeX = fade(fracX);
        double fadeZ = fade(fracZ);

        return lerp(lerp(dotTL, dotTR, fadeX), lerp(dotBL, dotBR, fadeX), fadeZ);
    }

    private static double dotProductOfGradientAndDistance(int hash, double x, double z) {
        switch (hash & 7) {
        case 0:  return +x    ;
        case 1:  return -x    ;
        case 2:  return    + z;
        case 3:  return    - z;
        case 4:  return +x + z;
        case 5:  return -x + z;
        case 6:  return +x - z;
        case 7:  return -x - z;
        default: return 0.0;
        }
    }
}