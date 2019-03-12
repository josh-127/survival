package dev.josh127.libwin32;

public abstract class PodStruct extends NativePtr {
    public PodStruct(long address) {
        super(address);
    }

    @Override
    protected void finalize() throws Throwable {
        free();
    }

    private native void free();
}