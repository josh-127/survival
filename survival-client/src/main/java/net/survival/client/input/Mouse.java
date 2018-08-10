package net.survival.client.input;

public final class Mouse
{
    static double x;
    static double y;
    static double prevX;
    static double prevY;
    static boolean lmbDown;
    static boolean rmbDown;
    static boolean prevLmbDown;
    static boolean prevRmbDown;

    private Mouse() {}

    public static double getX() {
        return x;
    }

    public static double getY() {
        return y;
    }

    public static double getDeltaX() {
        return x - prevX;
    }

    public static double getDeltaY() {
        return y - prevY;
    }

    public static boolean isLmbDown() {
        return lmbDown;
    }

    public static boolean isLmbUp() {
        return !lmbDown;
    }

    public static boolean isLmbPressed() {
        return lmbDown && !prevLmbDown;
    }

    public static boolean isLmbReleased() {
        return !lmbDown && prevLmbDown;
    }

    public static boolean isRmbDown() {
        return rmbDown;
    }

    public static boolean isRmbUp() {
        return !rmbDown;
    }

    public static boolean isRmbPressed() {
        return rmbDown && !prevRmbDown;
    }

    public static boolean isRmbReleased() {
        return !rmbDown && prevRmbDown;
    }
}