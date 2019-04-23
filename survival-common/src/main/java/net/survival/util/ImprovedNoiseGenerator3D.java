package net.survival.util;

import static net.survival.util.ImprovedNoiseMapHelper.fade;
import static net.survival.util.ImprovedNoiseMapHelper.generatePermutations;
import static net.survival.util.MathEx.lerp;

public class ImprovedNoiseGenerator3D
{
    private final double scaleX;
    private final double scaleY;
    private final double scaleZ;
    private final int octaveCount;
    private final long seed;
    private final int[] permutations;

    public ImprovedNoiseGenerator3D(double scaleX, double scaleY, double scaleZ, int octaveCount,
            long seed)
    {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.scaleZ = scaleZ;
        this.octaveCount = octaveCount;
        this.seed = seed;

        permutations = generatePermutations(seed);
    }

    public void generate(DoubleMap3D map, double offsetX, double offsetY, double offsetZ) {
        var mapLengthX = map.getLengthX();
        var mapLengthY = map.getLengthY();
        var mapLengthZ = map.getLengthZ();

        for (var y = 0; y < mapLengthY; ++y) {
            for (var z = 0; z < mapLengthZ; ++z) {
                for (var x = 0; x < mapLengthX; ++x)
                    map.setPoint(x, y, z, 0.0);
            }
        }

        var frequency = 1.0;
        var amplitude = 1.0;
        var maxValue = 0.0;

        for (var o = 0; o < octaveCount; ++o) {
            var octaveScaleX = scaleX * frequency;
            var octaveScaleY = scaleY * frequency;
            var octaveScaleZ = scaleZ * frequency;

            for (var y = 0; y < mapLengthY; ++y) {
                var noisePosY = (offsetY + y) * octaveScaleY;

                for (var z = 0; z < mapLengthZ; ++z) {
                    var noisePosZ = (offsetZ + z) * octaveScaleZ;

                    for (var x = 0; x < mapLengthX; ++x) {
                        var noisePosX = (offsetX + x) * octaveScaleX;
                        var prevOctave = map.sampleNearest(x, y, z);
                        var octave = valueAt(noisePosX, noisePosY, noisePosZ) * amplitude;

                        map.setPoint(x, y, z, prevOctave + octave);
                    }
                }
            }

            maxValue += amplitude;
            amplitude *= 0.5;
            frequency *= 2.0;
        }

        if (octaveCount > 1) {
            for (var y = 0; y < mapLengthY; ++y) {
                for (var z = 0; z < mapLengthZ; ++z) {
                    for (var x = 0; x < mapLengthX; ++x) {
                        var value = map.sampleNearest(x, y, z);
                        map.setPoint(x, y, z, value / maxValue);
                    }
                }
            }
        }
    }

    private double valueAt(double x, double y, double z) {
        var floorX = Math.floor(x);
        var floorY = Math.floor(y);
        var floorZ = Math.floor(z);
        var indexX = ((int) floorX) & 255;
        var indexY = ((int) floorY) & 255;
        var indexZ = ((int) floorZ) & 255;

        var permY     = permutations[indexY];
        var permY_Z   = permutations[permY + indexZ];
        var permY_NZ  = permutations[permY + indexZ + 1];
        var permNY    = permutations[indexY + 1];
        var permNY_Z  = permutations[permNY + indexZ];
        var permNY_NZ = permutations[permNY + indexZ + 1];

        var hashBBL = permutations[permY_Z   + indexX    ];
        var hashBBR = permutations[permY_Z   + indexX + 1];
        var hashBFL = permutations[permY_NZ  + indexX    ];
        var hashBFR = permutations[permY_NZ  + indexX + 1];
        var hashTBL = permutations[permNY_Z  + indexX    ];
        var hashTBR = permutations[permNY_Z  + indexX + 1];
        var hashTFL = permutations[permNY_NZ + indexX    ];
        var hashTFR = permutations[permNY_NZ + indexX + 1];

        var fracX = x - floorX;
        var fracY = y - floorY;
        var fracZ = z - floorZ;

        var dotBBL = dotProductOfGradientAndDistance(hashBBL, fracX,       fracY,       fracZ      );
        var dotBBR = dotProductOfGradientAndDistance(hashBBR, fracX - 1.0, fracY,       fracZ      );
        var dotBFL = dotProductOfGradientAndDistance(hashBFL, fracX,       fracY,       fracZ - 1.0);
        var dotBFR = dotProductOfGradientAndDistance(hashBFR, fracX - 1.0, fracY,       fracZ - 1.0);
        var dotTBL = dotProductOfGradientAndDistance(hashTBL, fracX,       fracY - 1.0, fracZ      );
        var dotTBR = dotProductOfGradientAndDistance(hashTBR, fracX - 1.0, fracY - 1.0, fracZ      );
        var dotTFL = dotProductOfGradientAndDistance(hashTFL, fracX,       fracY - 1.0, fracZ - 1.0);
        var dotTFR = dotProductOfGradientAndDistance(hashTFR, fracX - 1.0, fracY - 1.0, fracZ - 1.0);

        var fadeX = fade(fracX);
        var fadeY = fade(fracY);
        var fadeZ = fade(fracZ);

        return
            lerp(
                lerp(
                    lerp(dotBBL, dotBBR, fadeX),
                    lerp(dotBFL, dotBFR, fadeX),
                    fadeZ),
                lerp(
                    lerp(dotTBL, dotTBR, fadeX),
                    lerp(dotTFL, dotTFR, fadeX),
                    fadeZ),
                fadeY);
    }

    private static double dotProductOfGradientAndDistance(int hash, double x, double y, double z) {
        switch (hash & 15) {
        case 0:  return +x +y   ;
        case 1:  return -x +y   ;
        case 2:  return +x -y   ;
        case 3:  return -x -y   ;
        case 4:  return +x    +z;
        case 5:  return -x    +z;
        case 6:  return +x    -z;
        case 7:  return -x    -z;
        case 8:  return    +y +z;
        case 9:  return    -y +z;
        case 10: return    +y -z;
        case 11: return    -y -z;
        case 12: return +x +y   ;
        case 13: return    -y +z;
        case 14: return -x +y   ;
        case 15: return    -y -z;
        default: return 0.0;
        }
    }

    public double getScaleX() {
        return scaleX;
    }

    public double getScaleY() {
        return scaleY;
    }

    public double getScaleZ() {
        return scaleZ;
    }

    public int getOctaveCount() {
        return octaveCount;
    }

    public long getSeed() {
        return seed;
    }
}