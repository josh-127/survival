package net.survival.client.graphics2.internal;

import net.survival.client.util.Mat4;
import net.survival.client.util.Vec2;
import net.survival.client.util.Vec3;
import net.survival.client.util.Vec4;

/**
 * Represents an output stream targeting the graphics device.
 */
public interface GraphicsDataOutputStream
{
    /**
     * Writes a byte to the graphics device.
     * 
     * @param value the byte
     */
    void writeByte(byte value);

    /**
     * Writes a short to the graphics device.
     * 
     * @param value the short
     */
    void writeShort(short value);

    /**
     * Writes an integer to the graphics device.
     * 
     * @param value the integer
     */
    void writeInt(int value);

    /**
     * Writes a float to the graphics device.
     * 
     * @param value the float
     */
    void writeFloat(float value);

    /**
     * Writes a Vec2 to the graphics device.
     * 
     * @param vec the Vec2
     */
    void writeVec2(Vec2 vec);

    /**
     * Writes a Vec3 to the graphics device.
     * 
     * @param vec the Vec3
     */
    void writeVec3(Vec3 vec);

    /**
     * Writes a Vec4 to the graphics device.
     * 
     * @param vec the Vec4
     */
    void writeVec4(Vec4 vec);

    /**
     * Writes a Mat4 to the graphics device in row-major order.
     * 
     * @param mat the Mat4
     */
    void writeMat4(Mat4 mat);

    /**
     * Writes a Mat4 to the graphics device.
     * 
     * @param mat           the Mat4
     * @param isColumnMajor true to write mat in column-major order; false to write
     *                      mat in row-major order
     */
    void writeMat4(Mat4 mat, boolean isColumnMajor);
}