package net.survival.graphics.d3d11;

/**
 * Represents a data structure used in a graphics device.
 */
public interface Uniform
{
    /**
     * Gets the size of the uniform in bytes.
     * 
     * @return the size of the uniform in bytes
     */
    int getSize();

    /**
     * Overwrites uniform data to this Uniform contained in the graphics device.
     * 
     * @param uniform the uniform data
     */
    void writeUniform(WritableGraphicsData uniform);
}