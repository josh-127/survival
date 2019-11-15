package net.survival.graphics.d3d11.impl;

import dev.josh127.libwin32.D3D11;
import net.survival.graphics.d3d11.VertexBuffer;
import net.survival.graphics.d3d11.WritableGraphicsData;

class D3D11VertexBuffer implements VertexBuffer
{
    final D3D11.DeviceContext context;
    final D3D11.Buffer buffer;
    final int vertexCount;
    final int vertexSize;

    D3D11VertexBuffer(D3D11.DeviceContext context, D3D11.Buffer buffer, int vertexCount, int vertexSize) {
        this.context = context;
        this.buffer = buffer;
        this.vertexCount = vertexCount;
        this.vertexSize = vertexSize;
    }

    @Override
    public int getVertexCount() {
        return vertexCount;
    }

    @Override
    public void writeVertices(WritableGraphicsData vertices) {
        D3D11GraphicsDataOutputStream out = new D3D11GraphicsDataOutputStream(vertexCount * vertexSize);
        vertices.writeGraphicsData(out);
        context.updateSubresource(buffer, 0, null, out.buffer, 0, 0);
    }
}