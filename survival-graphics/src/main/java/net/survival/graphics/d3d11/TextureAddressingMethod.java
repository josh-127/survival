package net.survival.graphics.d3d11;

/**
 * Contains texture addressing methods.
 */
public enum TextureAddressingMethod
{
    /**
     * Tiles the texture at every (u, v) integer junction.
     */
    WRAP,

    /**
     * Clamps sampled texture coordinates to (0.0, 0.0) and (1.0, 1.0).
     */
    CLAMP
}