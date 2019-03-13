package net.survival.client.graphics2.internal;

/**
 * Represents a texture with an RGBA8 format.
 */
public interface Rgba8Texture extends Texture
{
    /**
     * Writes texels to the RGBA8Texture.
     * 
     * @param texels the texels to write
     */
    void writeTexels(int[] texels);
}