package net.survival.util;

import java.util.Random;

final class ImprovedNoiseMapHelper
{
    private ImprovedNoiseMapHelper() {}
    
    public static int[] generatePermutations(long seed)
    {
        int[] permutations = new int[512];
        
        for (int i = 0; i < 256; ++i)
            permutations[i] = i;

        Random rand = new Random(seed);
        for (int i = 0; i < 256; ++i) {
            int swapIndex = rand.nextInt(256 - i) + i;
            int temp = permutations[i];
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