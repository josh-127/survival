package net.survival.client.graphics2.internal.d3d11;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import net.survival.client.graphics2.internal.GraphicsDataOutputStream;
import net.survival.client.util.Mat4;
import net.survival.client.util.Vec2;
import net.survival.client.util.Vec3;
import net.survival.client.util.Vec4;

/**
 * Represents an output stream targeting the graphics device.
 */
class D3D11GraphicsDataOutputStream implements GraphicsDataOutputStream
{
    final ByteBuffer buffer;

    /**
     * Constructs a D3D11GraphicsDataOutputStream.
     * 
     * @param capacity the capacity of the output stream
     */
    public D3D11GraphicsDataOutputStream(int capacity) {
        buffer = ByteBuffer.allocateDirect(capacity);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
    }

    /**
     * Writes a byte to the graphics device.
     * 
     * @param value the byte
     */
    @Override
    public void writeByte(byte value) {
        buffer.put(value);
    }

    /**
     * Writes a short to the graphics device.
     * 
     * @param value the short
     */
    @Override
    public void writeShort(short value) {
        buffer.putShort(value);
    }

    /**
     * Writes a integer to the graphics device.
     * 
     * @param value the integer
     */
    @Override
    public void writeInt(int value) {
        buffer.putInt(value);
    }

    /**
     * Writes a float to the graphics device.
     * 
     * @param value the float
     */
    @Override
    public void writeFloat(float value) {
        buffer.putFloat(value);
    }

    /**
     * Writes a Vec2 to the graphics device.
     * 
     * @param value the Vec2
     */
    @Override
    public void writeVec2(Vec2 vec) {
        buffer.putFloat(vec.x);
        buffer.putFloat(vec.y);
    }

    /**
     * Writes a Vec3 to the graphics device.
     * 
     * @param value the Vec3
     */
    @Override
    public void writeVec3(Vec3 vec) {
        buffer.putFloat(vec.x);
        buffer.putFloat(vec.y);
        buffer.putFloat(vec.z);
    }

    /**
     * Writes a Vec4 to the graphics device.
     * 
     * @param value the Vec4
     */
    @Override
    public void writeVec4(Vec4 vec) {
        buffer.putFloat(vec.x);
        buffer.putFloat(vec.y);
        buffer.putFloat(vec.z);
        buffer.putFloat(vec.w);
    }

    /**
     * Writes a Mat4 to the graphics device in row-major order.
     * 
     * @param mat the Mat4
     */
    @Override
    public void writeMat4(Mat4 mat) {
        writeMat4(mat, false);
    }

    /**
     * Writes a Mat4 to the graphics device.
     * 
     * @param mat           the Mat4
     * @param isColumnMajor true to write mat in column-major order; false to write
     *                      mat in row-major order
     */
    @Override
    public void writeMat4(Mat4 mat, boolean isColumnMajor) {
        if (isColumnMajor) {
            buffer.putFloat(mat.m11);
            buffer.putFloat(mat.m21);
            buffer.putFloat(mat.m31);
            buffer.putFloat(mat.m41);
            buffer.putFloat(mat.m12);
            buffer.putFloat(mat.m22);
            buffer.putFloat(mat.m32);
            buffer.putFloat(mat.m42);
            buffer.putFloat(mat.m13);
            buffer.putFloat(mat.m23);
            buffer.putFloat(mat.m33);
            buffer.putFloat(mat.m43);
            buffer.putFloat(mat.m14);
            buffer.putFloat(mat.m24);
            buffer.putFloat(mat.m34);
            buffer.putFloat(mat.m44);
        }
        else {
            buffer.putFloat(mat.m11);
            buffer.putFloat(mat.m12);
            buffer.putFloat(mat.m13);
            buffer.putFloat(mat.m14);
            buffer.putFloat(mat.m21);
            buffer.putFloat(mat.m22);
            buffer.putFloat(mat.m23);
            buffer.putFloat(mat.m24);
            buffer.putFloat(mat.m31);
            buffer.putFloat(mat.m32);
            buffer.putFloat(mat.m33);
            buffer.putFloat(mat.m34);
            buffer.putFloat(mat.m41);
            buffer.putFloat(mat.m42);
            buffer.putFloat(mat.m43);
            buffer.putFloat(mat.m44);
        }
    }
}