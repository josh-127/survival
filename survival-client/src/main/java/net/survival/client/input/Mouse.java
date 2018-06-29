package net.survival.client.input;

public final class Mouse
{
    static double x;
    static double y;
    static double prevX;
    static double prevY;
    
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
}