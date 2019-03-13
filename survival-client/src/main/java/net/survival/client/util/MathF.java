package net.survival.client.util;

/**
 * Contains mathematical functions for 32-bit floating point numbers.
 */
public final class MathF
{
    private MathF() {
    }

    public static final float PIF = 3.14159265f;

    /**
     * Converts degrees to radians.
     * 
     * @param deg the degree
     * @return the radian
     */
    public static float degToRadf(float deg) {
        return deg * PIF / 180.0f;
    }

    /**
     * Returns the floor of a given value.
     * 
     * @param value the value
     * @return the floor of value
     */
    public static float floorf(float value) {
        return (float) Math.floor((float) value);
    }

    /**
     * Returns the ceiling of a given value.
     * 
     * @param value the value
     * @return the ceiling of value
     */
    public static float ceilf(float value) {
        return (float) Math.ceil((float) value);
    }

    /**
     * Returns the smaller number.
     * 
     * @param a the first number
     * @param b the second number
     * @return a if a <= b; otherwise b
     */
    public static float minf(float a, float b) {
        return Math.min(a, b);
    }

    /**
     * Returns the larger number.
     * 
     * @param a the first number
     * @param b the second number
     * @return a if a >= b; otherwise b
     */
    public static float maxf(float a, float b) {
        return Math.max(a, b);
    }

    /**
     * Clamps a given value between two values.
     * 
     * @param value the value
     * @param min   the minimum value
     * @param max   the maximum value
     * @return a clamped number of value between min and max
     */
    public static float clampf(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }

    /**
     * Returns the absolute value of a given value.
     * 
     * @param value the value
     * @return the absolute value of value
     */
    public static float absf(float value) {
        return Math.abs(value);
    }

    /**
     * Returns the square root of a given value.
     * 
     * @param value the value
     * @return the square root of value
     */
    public static float sqrtf(float value) {
        return (float) Math.sqrt((float) value);
    }

    /**
     * Returns the sine of a given angle.
     * 
     * @param angle the angle in radians
     * @return the sine of angle
     */
    public static float sinf(float angle) {
        return (float) Math.sin((float) angle);
    }

    /**
     * Returns the cosine of a given angle.
     * 
     * @param angle the angle in radians
     * @return the cosine of angle
     */
    public static float cosf(float angle) {
        return (float) Math.cos((float) angle);
    }

    /**
     * Returns the tangent of a given angle.
     * 
     * @param angle the angle in radians
     * @return the tangent of angle
     */
    public static float tanf(float angle) {
        return (float) Math.tan((float) angle);
    }

    /**
     * Returns the inverse sine of a given value.
     * 
     * @param oppositeOverHypotenuse the value
     * @return the inverse sine of oppositeOverHypotenuse
     */
    public static float asinf(float oppositeOverHypotenuse) {
        return (float) Math.asin(oppositeOverHypotenuse);
    }

    /**
     * Returns the inverse cosine of a given value.
     * 
     * @param adjacentOverHypotenuse the value
     * @return the inverse cosine of adjacentOverHypotenuse
     */
    public static float acosf(float adjacentOverHypotenuse) {
        return (float) Math.acos(adjacentOverHypotenuse);
    }

    /**
     * Returns the inverse tangent of a given value.
     * 
     * @param oppositeOverAdjacent the value
     * @return the inverse tangent of oppositeOverAdjacent
     */
    public static float atanf(float oppositeOverAdjacent) {
        return (float) Math.atan(oppositeOverAdjacent);
    }

    /**
     * Returns the polar angle of a given rectangular coordinate.
     * 
     * @param y the y-coordinate
     * @param x the x-coordinate
     * @return the polar angle of (x, y)
     */
    public static float atan2f(float y, float x) {
        return (float) Math.atan2((float) y, (float) x);
    }
}