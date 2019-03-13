package net.survival.client.graphics2.internal;

import net.survival.client.util.Rect;
import net.survival.client.util.Vec2;

/**
 * Represents a two-dimensional target that a graphics device can render to.
 */
public interface RenderTarget
{
    /**
     * Gets the width of the RenderTarget.
     * 
     * @return the width of the RenderTarget
     */
    int getWidth();

    /**
     * Gets the height of the RenderTarget.
     * 
     * @return the height of the RenderTarget
     */
    int getHeight();

    /**
     * Gets the size of the RenderTarget.
     * 
     * @return the size of the RenderTarget
     */
    Rect getSize();

    /**
     * Gets the size of the RenderTarget represented as a Vec2 where the x-component
     * is equal to the width and the y-component is equal to the height.
     * 
     * @return the size of the RenderTarget represented as a Vec2
     */
    Vec2 getSizeF();
}