package dev.josh127.libwin32;

public final class Rect extends PodStruct
{
    private Rect(long address) {
        super(address);
    }

    public static native Rect create(int left, int top, int right, int bottom);
}