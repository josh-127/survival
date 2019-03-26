package net.survival.util;

import java.util.Date;

public class Random64
{
    private static final long MULTIPLIER = 6364136223846793005L;
    private static final long INCREMENT = 1442695040888963407L;

    private long seed;

    public Random64() {
        seed = new Date().getTime();
    }

    public Random64(long seed) {
        this.seed = seed;
    }

    private void next() {
        seed *= MULTIPLIER;
        seed += INCREMENT;
    }

    public int nextInt() {
        next();
        return (int) seed;
    }

    public int nextInt(int bound) {
        assert bound > 0;

        if ((bound & -bound) == bound)
            return (int) ((bound * (long) nextInt()) >> 31);

        int bits;
        int value;
        do {
            bits = nextInt();
            value = bits % bound;
        }
        while (bits - value + (bound - 1) < 0);

        return value;
    }

    public long nextLong() {
        next();
        return seed;
    }
}