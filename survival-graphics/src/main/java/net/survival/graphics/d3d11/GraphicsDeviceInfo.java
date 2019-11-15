package net.survival.graphics.d3d11;

import net.survival.graphics.d3d11.util.Rect;
import net.survival.graphics.d3d11.util.Vec2;

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