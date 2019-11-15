package net.survival.graphics.d3d11.impl;

import dev.josh127.libwin32.D3D11;
import net.survival.graphics.d3d11.Uniform;
import net.survival.graphics.d3d11.WritableGraphicsData;

class D3D11Uniform implements Uniform
{
    final D3D11.DeviceContext context;
    final D3D11.Buffer buffer;
    final int size;

    D3D11Uniform(D3D11.DeviceContext context, D3D11.Buffer buffer, int size) {
        this.context = context;
        this.buffer = buffer;
        this.size = size;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public void writeUniform(WritableGraphicsData uniform) {
        D3D11GraphicsDataOutputStream out = new D3D11GraphicsDataOutputStream(size);
        uniform.writeGraphicsData(out);
        context.updateSubresource(buffer, 0, null, out.buffer, 0, 0);
    }
}