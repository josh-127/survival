package net.survival.graphics.d3d11;

/**
 * Represents a texture that can be rendered on.
 */
public interface ColorTarget extends RenderTarget
{
    /**
     * Gets the texture of the color target.
     * 
     * @return the texture of the color target
     */
    Texture getTexture();
}