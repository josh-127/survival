package net.survival.world.chunk;

public class DefaultChunkDatabase implements AsyncChunkProvider
{
    public DefaultChunkDatabase() {}

    @Override
    public Chunk provideChunkAsync(int cx, int cz) {
        return null;
    }
}