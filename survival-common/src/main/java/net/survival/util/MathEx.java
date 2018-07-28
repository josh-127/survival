package net.survival.util;

public class MathEx
{
    private MathEx() {}

    public static double clamp(double value, double min, double max) {
        if (value < min)
            return min;

        if (value > max)
            return max;

        return value;
    }

    public static double lerp(double a, double b, double t) {
        return a + t * (b - a);
    }

    public static double sqrMagnitude(double x, double y) {
        return x * x + y * y;
    }

    public static double sqrMagnitude(double x, double y, double z) {
        return x * x + y * y + z * z;
    }

    public static double magnitude(double x, double y) {
        return Math.sqrt(sqrMagnitude(x, y));
    }

    public static double magnitude(double x, double y, double z) {
        return Math.sqrt(sqrMagnitude(x, y, z));
    }
}