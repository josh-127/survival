package net.survival.client.graphics.blockrenderer;

import net.survival.client.graphics.opengl.GLDisplayList.Builder;

public class SaplingRenderer extends BlockRenderer
{
    public SaplingRenderer() {
        super(true);
    }

    @Override
    public void pushNonCubic(int x, int y, int z, float u1, float v1, float u2, float v2,
            Builder builder)
    {
    }

    @Override
    public void pushTopFaces(int x, int y, int z, float u1, float v1, float u2, float v2,
            Builder builder)
    {
    }

    @Override
    public void pushBottomFaces(int x, int y, int z, float u1, float v1, float u2, float v2,
            Builder builder)
    {
    }

    @Override
    public void pushLeftFaces(int x, int y, int z, float u1, float v1, float u2, float v2,
            Builder builder)
    {
    }

    @Override
    public void pushRightFaces(int x, int y, int z, float u1, float v1, float u2, float v2,
            Builder builder)
    {
    }

    @Override
    public void pushFrontFaces(int x, int y, int z, float u1, float v1, float u2, float v2,
            Builder builder)
    {
    }

    @Override
    public void pushBackFaces(int x, int y, int z, float u1, float v1, float u2, float v2,
            Builder builder)
    {
    }
}