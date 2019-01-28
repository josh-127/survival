package net.survival.util;

import java.util.Random;

final class ImprovedNoiseMapHelper
{
    private ImprovedNoiseMapHelper() {}

    public static int[] generatePermutations(long seed) {
        var permutations = new int[512];

        for (var i = 0; i < 256; ++i)
            permutations[i] = i;

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

    public static double fade(double t) {
        return t * t * t * (t * (t * 6.0 - 15.0) + 10.0);
    }
}