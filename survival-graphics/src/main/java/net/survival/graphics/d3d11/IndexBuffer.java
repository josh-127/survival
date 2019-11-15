package net.survival.graphics.d3d11;

/**
 * IndexBuffer contains indices of vertices.
 */
public interface IndexBuffer
{
    /**
     * Gets the number of indices in the IndexBuffer.
     * 
     * @return the number of indices in the IndexBuffer
     */
    int getIndexCount();

    /**
     * Writes indices to the IndexBuffer
     * 
     * @param indices the indices to write
     */
    void writeIndices(short[] indices);
}