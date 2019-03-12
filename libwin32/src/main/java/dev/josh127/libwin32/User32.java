package dev.josh127.libwin32;

public final class User32
{
    private User32() {}

    static native int nativeInit();

    public static native NativePtr createWindow(
            int width,
            int height,
            String title);

    public static native boolean peekEvent();

    public static native void dispatchEvents();
}