package dev.josh127.libwin32;

public class NativePtr
{
    public static final NativePtr NULL = new NativePtr(0L);

    public final transient long address;

    public NativePtr(long address) {
        this.address = address;
    }
}