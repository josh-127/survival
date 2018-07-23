package net.survival.client.graphics.opengl;

import net.survival.client.graphics.GraphicsResource;

public class GLRasterizerState implements GraphicsResource
{
    private boolean cullModeSet;
    private boolean fillModeSet;
    private boolean frontFaceSet;
    private boolean scissorSet;
    private boolean viewportSet;

    public GLRasterizerState() {}

    @Override
    public void close() {
        if (cullModeSet)
            GLRasterizer.popCullMode();
        if (fillModeSet)
            GLRasterizer.popFillMode();
        if (frontFaceSet)
            GLRasterizer.popFrontFace();
        if (scissorSet)
            GLRasterizer.popScissor();
        if (viewportSet)
            GLRasterizer.popViewport();
    }

    public GLRasterizerState withCullMode(GLCullMode cullMode) {
        if (cullModeSet)
            throw new IllegalStateException("Cull mode is already set.");

        GLRasterizer.pushCullMode(cullMode);
        cullModeSet = true;
        return this;
    }

    public GLRasterizerState withFillMode(GLFillMode fillMode) {
        if (fillModeSet)
            throw new IllegalStateException("Fill mode is already set.");

        GLRasterizer.pushFillMode(fillMode);
        fillModeSet = true;
        return this;
    }

    public GLRasterizerState withFrontFace(GLFrontFace frontFace) {
        if (frontFaceSet)
            throw new IllegalStateException("Front face is already set.");

        GLRasterizer.pushFrontFace(frontFace);
        frontFaceSet = true;
        return this;
    }

    public GLRasterizerState withScissor(int x, int y, int width, int height) {
        if (scissorSet)
            throw new IllegalStateException("Scissor is already set.");

        GLRasterizer.pushScissor(x, y, width, height);
        scissorSet = true;
        return this;
    }

    public GLRasterizerState withViewport(int x, int y, int width, int height) {
        if (viewportSet)
            throw new IllegalStateException("Viewport is already set.");

        GLRasterizer.pushViewport(x, y, width, height);
        viewportSet = true;
        return this;
    }
}