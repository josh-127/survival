package net.survival.client.graphics.opengl;

import net.survival.client.graphics.GraphicsResource;

public final class GLState implements GraphicsResource
{
    private boolean fogSet;

    private boolean depthTestSet;
    private boolean depthFunctionSet;
    private boolean depthWriteMaskSet;

    private boolean cullModeSet;
    private boolean fillModeSet;
    private boolean frontFaceSet;
    private boolean scissorSet;
    private boolean viewportSet;

    @Override
    public void close() throws RuntimeException {
        if (fogSet)
            GLStateStack.popFog();

        if (depthTestSet)
            GLStateStack.popDepthTest();
        if (depthFunctionSet)
            GLStateStack.popDepthFunction();
        if (depthWriteMaskSet)
            GLStateStack.popDepthWriteMask();

        if (cullModeSet)
            GLStateStack.popCullMode();
        if (fillModeSet)
            GLStateStack.popFillMode();
        if (frontFaceSet)
            GLStateStack.popFrontFace();
        if (scissorSet)
            GLStateStack.popScissor();
        if (viewportSet)
            GLStateStack.popViewport();
    }

    public GLState useNoFog() {
        if (fogSet)
            throw new IllegalStateException("Fog state is already set.");

        GLStateStack.pushNoFog();
        fogSet = true;
        return this;
    }

    public GLState useLinearFog(float start, float end, float r, float g, float b, float a) {
        if (fogSet)
            throw new IllegalStateException("Fog state is already set.");

        GLStateStack.pushLinearFog(start, end, r, g, b, a);
        fogSet = true;
        return this;
    }

    public GLState useExpFog(float density, float r, float g, float b, float a) {
        if (fogSet)
            throw new IllegalStateException("Fog state is already set.");

        GLStateStack.pushExpFog(density, r, g, b, a);
        fogSet = true;
        return this;
    }

    public GLState useExp2Fog(float density, float r, float g, float b, float a) {
        if (fogSet)
            throw new IllegalStateException("Fog state is already set.");

        GLStateStack.pushExp2Fog(density, r, g, b, a);
        fogSet = true;
        return this;
    }

    public GLState withDepthTest(boolean enabled) {
        if (depthTestSet)
            throw new IllegalStateException("Depth test already set");

        GLStateStack.pushDepthTest(enabled);
        depthTestSet = true;
        return this;
    }

    public GLState withDepthFunction(GLDepthFunction depthFunction) {
        if (depthFunctionSet)
            throw new IllegalStateException("Depth function already set");

        GLStateStack.pushDepthFunction(depthFunction);
        depthFunctionSet = true;
        return this;
    }

    public GLState withDepthWriteMask(boolean enabled) {
        if (depthWriteMaskSet)
            throw new IllegalStateException("Depth write mask already set");

        GLStateStack.pushDepthWriteMask(enabled);
        depthWriteMaskSet = true;
        return this;
    }

    public GLState withCullMode(GLCullMode cullMode) {
        if (cullModeSet)
            throw new IllegalStateException("Cull mode is already set.");

        GLStateStack.pushCullMode(cullMode);
        cullModeSet = true;
        return this;
    }

    public GLState withFillMode(GLFillMode fillMode) {
        if (fillModeSet)
            throw new IllegalStateException("Fill mode is already set.");

        GLStateStack.pushFillMode(fillMode);
        fillModeSet = true;
        return this;
    }

    public GLState withFrontFace(GLFrontFace frontFace) {
        if (frontFaceSet)
            throw new IllegalStateException("Front face is already set.");

        GLStateStack.pushFrontFace(frontFace);
        frontFaceSet = true;
        return this;
    }

    public GLState withScissor(int x, int y, int width, int height) {
        if (scissorSet)
            throw new IllegalStateException("Scissor is already set.");

        GLStateStack.pushScissor(x, y, width, height);
        scissorSet = true;
        return this;
    }

    public GLState withViewport(int x, int y, int width, int height) {
        if (viewportSet)
            throw new IllegalStateException("Viewport is already set.");

        GLStateStack.pushViewport(x, y, width, height);
        viewportSet = true;
        return this;
    }
}