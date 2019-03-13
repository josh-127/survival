package net.survival.client.graphics2.internal.d3d11;

import dev.josh127.libwin32.D3D11;
import net.survival.client.graphics2.internal.DepthStencilTarget;
import net.survival.client.util.Rect;
import net.survival.client.util.Vec2;

/**
 * Represents a depth stencil target used for depth and stencil testing.
 */
class D3D11DepthStencilTarget implements DepthStencilTarget
{
    final D3D11.DepthStencilView view;
    final D3D11.Texture2D texture;
    final int width;
    final int height;

    /**
     * Constructs a D3D11DepthStencilTarget.
     * 
     * @param device  the underlying graphics device
     * @param texture the underlying texture
     * @param width   the width of the the depth-stencil target
     * @param height  the height of the depth-stencil target
     */
    D3D11DepthStencilTarget(D3D11.Device device, D3D11.Texture2D texture, int width, int height) {
        this.texture = texture;
        this.width = width;
        this.height = height;
        view = device.createDepthStencilView(texture, null);
    }

    /**
     * Gets the width of the RenderTarget.
     * 
     * @return the width of the RenderTarget
     */
    @Override
    public int getWidth() {
        return width;
    }

    /**
     * Gets the height of the RenderTarget.
     * 
     * @return the height of the RenderTarget
     */
    @Override
    public int getHeight() {
        return height;
    }

    /**
     * Gets the size of the RenderTarget.
     * 
     * @return the size of the RenderTarget
     */
    @Override
    public Rect getSize() {
        return new Rect(0, 0, width, height);
    }

    /**
     * Gets the size of the RenderTarget represented as a Vec2 where the x-component
     * is equal to the width and the y-component is equal to the height.
     * 
     * @return the size of the RenderTarget represented as a Vec2
     */
    @Override
    public Vec2 getSizeF() {
        return new Vec2(width, height);
    }
}