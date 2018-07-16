package net.survival.util;

public class Fnv1a
{
    private static final int PRIME = 0x1000193;
    private static final int OFFSET_BASIS = 0x811C9DC5;
    
    private Fnv1a() {}
    
    public static int hash(int a) {
        int result = OFFSET_BASIS;
        result ^= a;
        result *= PRIME;
        return result;
    }
    
    public static int hash(int a, int b) {
        int result = OFFSET_BASIS;
        result ^= a;
        result *= PRIME;
        result ^= b;
        result *= PRIME;
        return result;
    }
    
    public static int hash(int a, int b, int c) {
        int result = OFFSET_BASIS;
        result ^= a;
        result *= PRIME;
        result ^= b;
        result *= PRIME;
        result ^= c;
        result *= PRIME;
        return result;
    }
    
    public static int hash(int a, int b, int c, int d) {
        int result = OFFSET_BASIS;
        result ^= a;
        result *= PRIME;
        result ^= b;
        result *= PRIME;
        result ^= c;
        result *= PRIME;
        result ^= d;
        result *= PRIME;
        return result;
    }
}