package dev.josh127.libwin32;

public abstract class ComObject extends NativePtr
{
    public ComObject(long address) {
        super(address);
    }

    public native void release();
}