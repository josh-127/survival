package net.survival.client.input;

public final class Keyboard
{
    static boolean[] prevKeyState = new boolean[Key.getCachedValues().length];
    static boolean[] keyState = new boolean[Key.getCachedValues().length];

    private Keyboard() {}

    public static boolean isKeyDown(Key key) {
        return keyState[key.ordinal()];
    }

    public static boolean isKeyUp(Key key) {
        return !keyState[key.ordinal()];
    }

    public static boolean isKeyPressed(Key key) {
        return keyState[key.ordinal()] && !prevKeyState[key.ordinal()];
    }

    public static boolean isKeyReleased(Key key) {
        return !keyState[key.ordinal()] && prevKeyState[key.ordinal()];
    }
}