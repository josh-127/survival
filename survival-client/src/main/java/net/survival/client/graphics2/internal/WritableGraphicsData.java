package net.survival.client.graphics2.internal;

/**
 * Represents a graphics object that can be written to a graphics device.
 */
public interface WritableGraphicsData
{
    /**
     * Writes the data of this graphical object to a graphics device.
     * 
     * @param out the output stream targeting a graphics device
     */
    void writeGraphicsData(GraphicsDataOutputStream out);
}