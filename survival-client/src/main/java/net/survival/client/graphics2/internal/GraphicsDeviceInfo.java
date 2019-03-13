package net.survival.client.graphics2.internal;

import net.survival.client.util.Rect;
import net.survival.client.util.Vec2;

/**
 * GraphicsDeviceInfo contains information for a graphics device.
 */
public interface GraphicsDeviceInfo
{
    /**
     * Gets the width of the screen.
     * 
     * @return the width of the screen
     */
    int getWidth();

    /**
     * Gets the height of the screen.
     * 
     * @return the height of the screen
     */
    int getHeight();

    /**
     * Gets the size of the screen.
     * 
     * @return the size of the screen
     */
    Rect getSize();

    /**
     * Gets the size of the screen.
     * 
     * @return the size of the screen
     */
    Vec2 getSizeF();

    /**
     * Gets the aspect ratio of the screen.
     * 
     * @return the aspect ratio of the screen
     */
    float getAspectRatio();

    /**
     * Gets the maximum number of color targets the graphics device supports.
     * 
     * @return the maximum number of color targets the graphics device supports
     */
    int getMaxColorTargets();

    /**
     * Gets the maximum number of texture slots the graphics device supports.
     * 
     * @return the maximum number of texture slots the graphics device supports
     */
    int getMaxTextureSlots();

    /**
     * Gets the maximum number of vertex buffer slots the graphics device supports.
     * 
     * @return the maximum number of vertex buffer slots the graphics device
     *         supports
     */
    int getMaxVertexBufferSlots();
}