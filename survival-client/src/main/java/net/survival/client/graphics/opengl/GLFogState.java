package net.survival.client.graphics.opengl;

import net.survival.client.graphics.GraphicsResource;

public class GLFogState implements GraphicsResource
{
    private boolean fogSet;
    
    public GLFogState() {}

    @Override
    public void close() {
        GLFog.popFog();
    }
    
    public GLFogState useNoFog() {
        if (fogSet)
            throw new IllegalStateException("Fog state is already set.");
        
        GLFog.pushNoFog();
        fogSet = true;
        return this;
    }
    
    public GLFogState useLinearFog(float start, float end, float r, float g, float b, float a) {
        if (fogSet)
            throw new IllegalStateException("Fog state is already set.");
        
        GLFog.pushLinearFog(start, end, r, g, b, a);
        fogSet = true;
        return this;
    }
    
    public GLFogState useExpFog(float density, float r, float g, float b, float a) {
        if (fogSet)
            throw new IllegalStateException("Fog state is already set.");
        
        GLFog.pushExpFog(density, r, g, b, a);
        fogSet = true;
        return this;
    }
    
    public GLFogState useExp2Fog(float density, float r, float g, float b, float a) {
        if (fogSet)
            throw new IllegalStateException("Fog state is already set.");
        
        GLFog.pushExp2Fog(density, r, g, b, a);
        fogSet = true;
        return this;
    }
}