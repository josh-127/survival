package net.survival.util;

import java.util.Random;

public final class ImprovedNoiseGenerator {
    private ImprovedNoiseGenerator() {}

    public static void generate2D(
            double offsetX,
            double offsetZ,
            double scaleX,
            double scaleZ,
            int octaveCount,
            long seed,
            DoubleMap2D dest)
    {
        // TODO: Cache with a splay tree.
        var permutations = generatePermutations(seed);

        var lengthX = dest.getLengthX();
        var lengthZ = dest.getLengthZ();

        for (var z = 0; z < lengthZ; ++z) {
            for (var x = 0; x < lengthX; ++x) {
                dest.setPoint(x, z, 0.0);
            }
        }

        var frequency = 1.0;
        var amplitude = 1.0;
        var maxValue = 0.0;

        for (var o = 0; o < octaveCount; ++o) {
            var octaveScaleX = scaleX * frequency;
            var octaveScaleZ = scaleZ * frequency;

            for (var z = 0; z < lengthZ; ++z) {
                var noisePosZ = (offsetZ + z) * octaveScaleZ;

                for (var x = 0; x < lengthX; ++x) {
                    var noisePosX = (offsetX + x) * octaveScaleX;
                    var prevOctave = dest.sampleNearest(x, z);
                    var octave = valueAt2D(noisePosX, noisePosZ, permutations);

                    dest.setPoint(x, z, prevOctave + octave * amplitude);
                }
            }

            maxValue += amplitude;
            amplitude *= 0.5;
            frequency *= 2.0;
        }

        if (octaveCount > 1) {
            for (var z = 0; z < lengthZ; ++z) {
                for (var x = 0; x < lengthX; ++x) {
                    var value = dest.sampleNearest(x, z);
                    dest.setPoint(x, z, value / maxValue);
                }
            }
        }
    }

    public static DoubleMap2D generate2D(
            double offsetX,
            double offsetZ,
            int lengthX,
            int lengthZ,
            double scaleX,
            double scaleZ,
            int octaveCount,
            long seed)
    {
        var map = new DoubleMap2D(lengthX, lengthZ);
        generate2D(offsetX, offsetZ, scaleX, scaleZ, octaveCount, seed, map);

        return map;
    }

    public static void generate3D(
            double offsetX,
            double offsetY,
            double offsetZ,
            double scaleX,
            double scaleY,
            double scaleZ,
            int octaveCount,
            long seed,
            DoubleMap3D dest)
    {
        // TODO: Cache with a splay tree.
        var permutations = generatePermutations(seed);

        var lengthX = dest.getLengthX();
        var lengthY = dest.getLengthY();
        var lengthZ = dest.getLengthZ();

        for (var y = 0; y < lengthY; ++y) {
            for (var z = 0; z < lengthZ; ++z) {
                for (var x = 0; x < lengthX; ++x) {
                    dest.setPoint(x, y, z, 0.0);
                }
            }
        }

        var frequency = 1.0;
        var amplitude = 1.0;
        var maxValue = 0.0;

        for (var o = 0; o < octaveCount; ++o) {
            var octaveScaleX = scaleX * frequency;
            var octaveScaleY = scaleY * frequency;
            var octaveScaleZ = scaleZ * frequency;

            for (var y = 0; y < lengthY; ++y) {
                var noisePosY = (offsetY + y) * octaveScaleY;

                for (var z = 0; z < lengthZ; ++z) {
                    var noisePosZ = (offsetZ + z) * octaveScaleZ;

                    for (var x = 0; x < lengthX; ++x) {
                        var noisePosX = (offsetX + x) * octaveScaleX;
                        var prevOctave = dest.sampleNearest(x, y, z);
                        var octave = valueAt3D(
                                noisePosX, noisePosY, noisePosZ, permutations);

                        dest.setPoint(x, y, z, prevOctave + octave * amplitude);
                    }
                }
            }

            maxValue += amplitude;
            amplitude *= 0.5;
            frequency *= 2.0;
        }

        if (octaveCount > 1) {
            for (var y = 0; y < lengthY; ++y) {
                for (var z = 0; z < lengthZ; ++z) {
                    for (var x = 0; x < lengthX; ++x) {
                        var value = dest.sampleNearest(x, y, z);
                        dest.setPoint(x, y, z, value / maxValue);
                    }
                }
            }
        }
    }

    public static DoubleMap3D generate3D(
            double offsetX,
            double offsetY,
            double offsetZ,
            int lengthX,
            int lengthY,
            int lengthZ,
            double scaleX,
            double scaleY,
            double scaleZ,
            int octaveCount,
            long seed)
    {
        var map = new DoubleMap3D(lengthX, lengthY, lengthZ);
        generate3D(offsetX, offsetY, offsetZ, scaleX, scaleY, scaleZ, octaveCount, seed, map);

        return map;
    }

    private static double valueAt2D(double x, double z, int[] permutations) {
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

        var dotTL = gradDotDist2D(hashTL, fracX, fracZ);
        var dotTR = gradDotDist2D(hashTR, fracX - 1.0, fracZ);
        var dotBL = gradDotDist2D(hashBL, fracX, fracZ - 1.0);
        var dotBR = gradDotDist2D(hashBR, fracX - 1.0, fracZ - 1.0);

        var fadeX = fade(fracX);
        var fadeZ = fade(fracZ);

        return
                MathEx.lerp(
                        MathEx.lerp(dotTL, dotTR, fadeX),
                        MathEx.lerp(dotBL, dotBR, fadeX), fadeZ);
    }

    private static double valueAt3D(double x, double y, double z, int[] permutations) {
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

        var dotBBL = gradDotDist3D(hashBBL, fracX,       fracY,       fracZ      );
        var dotBBR = gradDotDist3D(hashBBR, fracX - 1.0, fracY,       fracZ      );
        var dotBFL = gradDotDist3D(hashBFL, fracX,       fracY,       fracZ - 1.0);
        var dotBFR = gradDotDist3D(hashBFR, fracX - 1.0, fracY,       fracZ - 1.0);
        var dotTBL = gradDotDist3D(hashTBL, fracX,       fracY - 1.0, fracZ      );
        var dotTBR = gradDotDist3D(hashTBR, fracX - 1.0, fracY - 1.0, fracZ      );
        var dotTFL = gradDotDist3D(hashTFL, fracX,       fracY - 1.0, fracZ - 1.0);
        var dotTFR = gradDotDist3D(hashTFR, fracX - 1.0, fracY - 1.0, fracZ - 1.0);

        var fadeX = fade(fracX);
        var fadeY = fade(fracY);
        var fadeZ = fade(fracZ);

        return
            MathEx.lerp(
                MathEx.lerp(
                    MathEx.lerp(dotBBL, dotBBR, fadeX),
                    MathEx.lerp(dotBFL, dotBFR, fadeX),
                    fadeZ),
                MathEx.lerp(
                    MathEx.lerp(dotTBL, dotTBR, fadeX),
                    MathEx.lerp(dotTFL, dotTFR, fadeX),
                    fadeZ),
                fadeY);
    }

    private static double gradDotDist2D(int hash, double x, double z) {
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

    private static double gradDotDist3D(int hash, double x, double y, double z) {
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

    private static int[] generatePermutations(long seed) {
        var permutations = new int[512];

        for (var i = 0; i < 256; ++i) {
            permutations[i] = i;
        }

        var rand = new Random(seed);
        for (var i = 0; i < 256; ++i) {
            var swapIndex = rand.nextInt(256 - i) + i;
            var temp = permutations[i];
            permutations[i] = permutations[swapIndex];
            permutations[swapIndex] = temp;
            permutations[i + 256] = permutations[i];
        }

        return permutations;
    }

    private static double fade(double t) {
        return t * t * t * (t * (t * 6.0 - 15.0) + 10.0);
    }
}