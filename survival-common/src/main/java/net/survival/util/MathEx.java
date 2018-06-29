package net.survival.util;

public class MathEx
{
    private MathEx() {}
    
    public static double clamp(double value, double min, double max) {
        if (value < min) return min;
        if (value > max) return max;
        return value;
    }
    
    public static double lerp(double a, double b, double t) {
        return a + t * (b - a);
    }
}