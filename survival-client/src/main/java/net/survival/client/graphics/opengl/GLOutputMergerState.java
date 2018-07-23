package net.survival.client.graphics.opengl;

import net.survival.client.graphics.GraphicsResource;

public class GLOutputMergerState implements GraphicsResource
{
    private boolean depthTestSet;
    private boolean depthFunctionSet;
    private boolean depthWriteMaskSet;

    public GLOutputMergerState() {}

    @Override
    public void close() {
        if (depthTestSet)
            GLOutputMerger.popDepthTest();
        if (depthFunctionSet)
            GLOutputMerger.popDepthFunction();
        if (depthWriteMaskSet)
            GLOutputMerger.popDepthWriteMask();
    }

    public GLOutputMergerState withDepthTest(boolean enabled) {
        if (depthTestSet)
            throw new IllegalStateException("Depth test already set");

        GLOutputMerger.pushDepthTest(enabled);
        depthTestSet = true;
        return this;
    }

    public GLOutputMergerState withDepthFunction(GLDepthFunction depthFunction) {
        if (depthFunctionSet)
            throw new IllegalStateException("Depth function already set");

        GLOutputMerger.pushDepthFunction(depthFunction);
        depthFunctionSet = true;
        return this;
    }

    public GLOutputMergerState withDepthWriteMask(boolean enabled) {
        if (depthWriteMaskSet)
            throw new IllegalStateException("Depth write mask already set");

        GLOutputMerger.pushDepthWriteMask(enabled);
        depthWriteMaskSet = true;
        return this;
    }
}