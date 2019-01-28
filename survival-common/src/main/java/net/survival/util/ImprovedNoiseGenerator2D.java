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
        for (var z = 0; z < map.lengthZ; ++z) {
            for (var x = 0; x < map.lengthX; ++x)
                map.setPoint(x, z, 0.0);
        }

        var octaveScale = 1.0;

        for (var o = 0; o < octaveCount; ++o) {
            var octaveScaleX = scaleX * octaveScale;
            var octaveScaleZ = scaleZ * octaveScale;
            var invOctaveScale = 1.0 / octaveScale;

            for (var z = 0; z < map.lengthZ; ++z) {
                var noisePosZ = (offsetZ + z) * octaveScaleZ;

                for (var x = 0; x < map.lengthX; ++x) {
                    var noisePosX = (offsetX + x) * octaveScaleX;
                    var prevOctave = map.sampleNearest(x, z);
                    var currentOctave = valueAt(noisePosX, noisePosZ) * invOctaveScale;

                    map.setPoint(x, z, prevOctave + currentOctave);
                }
            }

            octaveScale *= 2.0;
        }

        if (octaveCount > 1) {
            var clamper = 1.0 / (2.0 - 1.0 / octaveScale);
            for (var z = 0; z < map.lengthZ; ++z) {
                for (var x = 0; x < map.lengthX; ++x)
                    map.setPoint(x, z, map.sampleNearest(x, z) * clamper);
            }
        }
    }

    private double valueAt(double x, double z) {
        var floorX = Math.floor(x);
        var floorZ = Math.floor(z);
        var indexX = ((int) floorX) & 255;
        var indexZ = ((int) floorZ) & 255;

        var hashTL = permutations[permutations[indexZ] + indexX];
        var hashTR = permutations[permutations[indexZ] + indexX + 1];
        var hashBL = permutations[permutations[indexZ + 1] + indexX];
        var hashBR = permutations[permutations[indexZ + 1] + indexX + 1];

        var fracX = x - floorX;
        var fracZ = z - floorZ;

        var dotTL = dotProductOfGradientAndDistance(hashTL, fracX, fracZ);
        var dotTR = dotProductOfGradientAndDistance(hashTR, fracX - 1.0, fracZ);
        var dotBL = dotProductOfGradientAndDistance(hashBL, fracX, fracZ - 1.0);
        var dotBR = dotProductOfGradientAndDistance(hashBR, fracX - 1.0, fracZ - 1.0);

        var fadeX = fade(fracX);
        var fadeZ = fade(fracZ);

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