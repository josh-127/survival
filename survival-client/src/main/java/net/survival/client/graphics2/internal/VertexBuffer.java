package net.survival.client.graphics2.internal;

/**
 * Represents an array of data structures (vertices) used in a graphics device.
 */
public interface VertexBuffer
{
    /**
     * Gets the number of vertices in the VertexBuffer.
     * 
     * @return the number of vertices in the VertexBuffer
     */
    int getVertexCount();

    /**
     * Overwrites vertices to the VertexBuffer contained in the graphics device.
     * 
     * @param vertices the vertices
     */
    void writeVertices(WritableGraphicsData vertices);
}