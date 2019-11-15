package net.survival.graphics.d3d11.impl;

import java.nio.ByteBuffer;

import dev.josh127.libwin32.D3D11;
import net.survival.graphics.d3d11.util.Rect;
import net.survival.graphics.d3d11.util.Vec2;
import net.survival.graphics.d3d11.Rgba8Texture;

class D3D11Rgba8Texture extends D3D11Texture implements Rgba8Texture
{
    D3D11Rgba8Texture(
            D3D11.Device device,
            D3D11.Texture2D texture,
            int width,
            int height,
            boolean genMips)
    {
        super(device, texture, width, height, genMips);
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public Rect getSize() {
        return new Rect(0, 0, width, height);
    }

    @Override
    public Vec2 getSizeF() {
        return new Vec2(width, height);
    }

    @Override
    public boolean hasAlpha() {
        return true;
    }

    @Override
    public boolean hasMipMaps() {
        return hasMipMaps;
    }

    @Override
    public void writeTexels(int[] texels) {
        ByteBuffer buffer = ByteBuffer.allocateDirect(4 * texels.length);

        for (int texel : texels) {
            buffer.put((byte) ((texel & 0x00FF0000) >> 16));
            buffer.put((byte) ((texel & 0x0000FF00) >> 8));
            buffer.put((byte) texel);
            buffer.put((byte) ((texel & 0xFF000000) >> 24));
        }

        context.updateSubresource(texture, 0, null, buffer, 4 * width, 0);
    }
}