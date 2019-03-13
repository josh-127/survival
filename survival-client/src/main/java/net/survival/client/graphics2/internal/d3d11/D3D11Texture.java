package net.survival.client.graphics2.internal.d3d11;

import dev.josh127.libwin32.D3D11;

class D3D11Texture
{
    final D3D11.DeviceContext context;
    final D3D11.Texture2D texture;
    final D3D11.ShaderResourceView view;
    final int width;
    final int height;
    final boolean hasMipMaps;

    protected D3D11Texture(
            D3D11.Device device,
            D3D11.Texture2D texture,
            int width,
            int height,
            boolean genMips)
    {
        this.context = device.getImmediateContext();
        this.texture = texture;
        this.width = width;
        this.height = height;
        view = device.createShaderResourceView(texture, null);
        hasMipMaps = genMips;
    }
}