package net.survival.util;

public class MathEx {
    private MathEx() {}

    public static double clamp(double value, double min, double max) {
        if (value < min) {
            return min;
        }
        if (value > max) {
            return max;
        }
        return value;
    }

    public static double lerp(double a, double b, double t) {
        return a + t * (b - a);
    }

    public static float lerp(float a, float b, float t) {
        return a + t * (b - a);
    }

    public static double sqrLength(double x, double y) {
        return x * x + y * y;
    }

    public static double sqrLength(double x, double y, double z) {
        return x * x + y * y + z * z;
    }

    public static float sqrLength(float x, float y) {
        return x * x + y * y;
    }

    public static float sqrLength(float x, float y, float z) {
        return x * x + y * y + z * z;
    }

    public static double length(double x, double y) {
        return Math.sqrt(sqrLength(x, y));
    }

    public static double length(double x, double y, double z) {
        return Math.sqrt(sqrLength(x, y, z));
    }

    public static float length(float x, float y) {
        return (float) Math.sqrt(sqrLength(x, y));
    }

    public static float length(float x, float y, float z) {
        return (float) Math.sqrt(sqrLength(x, y, z));
    }

    public static double crossX(double x1, double y1, double z1, double x2, double y2, double z2) {
        return (y1 * z2) - (z1 * y2);
    }

    public static double crossY(double x1, double y1, double z1, double x2, double y2, double z2) {
        return (z1 * x2) - (x1 * z2);
    }

    public static double crossZ(double x1, double y1, double z1, double x2, double y2, double z2) {
        return (x1 * y2) - (y1 * x2);
    }

    public static float crossX(float x1, float y1, float z1, float x2, float y2, float z2) {
        return (y1 * z2) - (z1 * y2);
    }

    public static float crossY(float x1, float y1, float z1, float x2, float y2, float z2) {
        return (z1 * x2) - (x1 * z2);
    }

    public static float crossZ(float x1, float y1, float z1, float x2, float y2, float z2) {
        return (x1 * y2) - (y1 * x2);
    }
}