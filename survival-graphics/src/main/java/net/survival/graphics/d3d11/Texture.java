package net.survival.graphics.d3d11;

import net.survival.graphics.d3d11.util.Rect;
import net.survival.graphics.d3d11.util.Vec2;

/**
 * Represents a two-dimensional array that is indexed through floating-point
 * numbers. Textures can be sampled, and mipmaps are used to improve sampling
 * performance. Textures are typically used for images, uv-mapped images, and
 * render targets.
 */
public interface Texture
{
    /**
     * Gets the width of this texture.
     * 
     * @return the width of this texture
     */
    int getWidth();

    /**
     * Gets the height of this texture.
     * 
     * @return the height of this texture
     */
    int getHeight();

    /**
     * Gets the size of this texture as a Rect.
     * 
     * @return the size of this texture
     */
    Rect getSize();

    /**
     * Gets the size of this texture as a Vec2.
     * 
     * @return the size of this texture
     */
    Vec2 getSizeF();

    /**
     * Determines if the texture has an alpha channel.
     * 
     * @return true if the texture has an alpha channel; otherwise false
     */
    boolean hasAlpha();

    /**
     * Determines if the texture has mipmaps.
     * 
     * @return true if the texture has mipmaps; otherwise false
     */
    boolean hasMipMaps();
}