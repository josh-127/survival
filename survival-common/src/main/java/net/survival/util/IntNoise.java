package net.survival.util;

public class IntNoise
{
    private IntNoise() {}

    public static int white(int x, long seed) {
        // Based off of MurmurHash
        long hash = seed;

        long k = rol15((int) x * 0xCC9E2D51L) * 0x1B873593L;
        hash = rol13(hash ^ k) * 5L + 0xE6546B64L;

        hash ^= 4L;
        hash ^= (hash >>> 16L);
        hash *= 0x85EBCA6BL;
        hash ^= (hash >>> 13L);
        hash *= 0xC2B2AE35L;
        hash ^= (hash >>> 16L);

        return (int) hash;
    }

    public static int white2D(int x, int y, long seed) {
        // Based off of MurmurHash
        long hash = seed;

        long k = rol15((int) x * 0xCC9E2D51L) * 0x1B873593L;
        hash = rol13(hash ^ k) * 5L + 0xE6546B64L;

        k = rol15((int) y * 0xCC9E2D51L) * 0x1B873593L;
        hash = rol13(hash ^ k) * 5L + 0xE6546B64L;

        hash ^= 8L;
        hash ^= (hash >>> 16L);
        hash *= 0x85EBCA6BL;
        hash ^= (hash >>> 13L);
        hash *= 0xC2B2AE35L;
        hash ^= (hash >>> 16L);

        return (int) hash;
    }

    public static int white3D(int x, int y, int z, long seed) {
        // Based off of MurmurHash
        long hash = seed;

        long k = rol15((int) x * 0xCC9E2D51L) * 0x1B873593L;
        hash = rol13(hash ^ k) * 5L + 0xE6546B64L;

        k = rol15((int) y * 0xCC9E2D51L) * 0x1B873593L;
        hash = rol13(hash ^ k) * 5L + 0xE6546B64L;

        k = rol15((int) z * 0xCC9E2D51L) * 0x1B873593L;
        hash = rol13(hash ^ k) * 5L + 0xE6546B64L;

        hash ^= 12L;
        hash ^= (hash >>> 16L);
        hash *= 0x85EBCA6BL;
        hash ^= (hash >>> 13L);
        hash *= 0xC2B2AE35L;
        hash ^= (hash >>> 16L);

        return (int) hash;
    }

    private static long rol15(long value) {
        return (value << 15L) | (value >>> 17L);
    }

    private static long rol13(long value) {
        return (value << 13L) | (value >>> 19L);
    }
}