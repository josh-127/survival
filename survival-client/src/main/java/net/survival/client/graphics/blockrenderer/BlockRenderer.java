package net.survival.client.graphics.blockrenderer;

import net.survival.client.graphics.opengl.GLDisplayList;

public abstract class BlockRenderer
{
    public final boolean nonCubic;

    public BlockRenderer(boolean nonCubic) {
        this.nonCubic = nonCubic;
    }

    public abstract void pushNonCubic(int x, int y, int z, float u1, float v1, float u2, float v2,
            GLDisplayList.Builder builder);

    public abstract void pushTopFaces(int x, int y, int z, float u1, float v1, float u2, float v2,
            GLDisplayList.Builder builder);

    public abstract void pushBottomFaces(int x, int y, int z, float u1, float v1, float u2, float v2,
            GLDisplayList.Builder builder);

    public abstract void pushLeftFaces(int x, int y, int z, float u1, float v1, float u2, float v2,
            GLDisplayList.Builder builder);

    public abstract void pushRightFaces(int x, int y, int z, float u1, float v1, float u2, float v2,
            GLDisplayList.Builder builder);

    public abstract void pushFrontFaces(int x, int y, int z, float u1, float v1, float u2, float v2,
            GLDisplayList.Builder builder);

    public abstract void pushBackFaces(int x, int y, int z, float u1, float v1, float u2, float v2,
            GLDisplayList.Builder builder);
}