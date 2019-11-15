package net.survival.graphics.d3d11.impl;

import dev.josh127.libwin32.D3D11;
import net.survival.graphics.d3d11.util.Rect;
import net.survival.graphics.d3d11.util.Vec2;
import net.survival.graphics.d3d11.ColorTarget;
import net.survival.graphics.d3d11.Texture;

/**
 * Represents a texture that can be rendered on.
 */
class D3D11ColorTarget implements ColorTarget
{
    final D3D11.RenderTargetView view;
    final D3D11Rgba8Texture texture;
    final int width;
    final int height;

    /**
     * Constructs a D3D11ColorTarget.
     * 
     * @param device  the underlying graphics device
     * @param texture the underlying texture
     * @param width   the width of the color target
     * @param height  the height of the color target
     */
    D3D11ColorTarget(D3D11.Device device, D3D11.Texture2D texture, int width, int height) {
        this.texture = new D3D11Rgba8Texture(device, texture, width, height, false);
        this.width = width;
        this.height = height;
        view = device.createRenderTargetView(texture, null);
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

    /**
     * Gets the texture of the color target.
     * 
     * @return the texture of the color target
     */
    @Override
    public Texture getTexture() {
        return texture;
    }
}