package net.survival.client.graphics2.internal.d3d11;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import dev.josh127.libwin32.D3D11;
import net.survival.client.graphics2.internal.IndexBuffer;

/**
 * IndexBuffer contains indices of vertices.
 */
class D3D11IndexBuffer implements IndexBuffer
{
    final D3D11.DeviceContext context;
    final D3D11.Buffer buffer;
    final int indexCount;

    /**
     * Constructs a D3D11IndexBuffer.
     * 
     * @param context    the underlying graphics device context
     * @param buffer     the underlying index buffer
     * @param indexCount the number of indices
     */
    D3D11IndexBuffer(D3D11.DeviceContext context, D3D11.Buffer buffer, int indexCount) {
        this.context = context;
        this.buffer = buffer;
        this.indexCount = indexCount;
    }

    /**
     * Gets the number of indices in the IndexBuffer.
     * 
     * @return the number of indices in the IndexBuffer
     */
    @Override
    public int getIndexCount() {
        return indexCount;
    }

    /**
     * Writes indices to the IndexBuffer
     * 
     * @param indices the indices to write
     */
    @Override
    public void writeIndices(short[] indices) {
        ByteBuffer buffer = ByteBuffer.allocateDirect(2 * indexCount);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        for (short index : indices) {
            buffer.putShort(index);
        }
        context.updateSubresource(this.buffer, 0, null, buffer, 0, 0);
    }
}