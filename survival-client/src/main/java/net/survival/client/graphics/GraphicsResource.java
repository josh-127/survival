package net.survival.client.graphics;

public interface GraphicsResource extends AutoCloseable
{
    void close() throws RuntimeException;
}