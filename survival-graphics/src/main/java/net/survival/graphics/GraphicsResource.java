package net.survival.graphics;

public interface GraphicsResource extends AutoCloseable
{
    void close() throws RuntimeException;
}