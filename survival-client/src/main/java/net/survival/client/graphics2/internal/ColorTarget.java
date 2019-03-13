package net.survival.client.graphics2.internal;

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