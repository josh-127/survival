package net.survival.graphics.d3d11;

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