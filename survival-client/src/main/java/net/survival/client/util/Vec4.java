package net.survival.client.util;

/**
 * Represents a four-dimensional vector.
 */
public final class Vec4
{
    public static final Vec4 ZERO = new Vec4(0.0f, 0.0f, 0.0f, 0.0f);
    public static final Vec4 ONE = new Vec4(1.0f, 1.0f, 1.0f, 1.0f);

    public final float x;
    public final float y;
    public final float z;
    public final float w;

    /**
     * Constructs a Vec4.
     * 
     * @param x the x-component
     * @param y the y-component
     * @param z the z-component
     * @param w the w-component
     */
    public Vec4(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    /**
     * Constructs a Vec4.
     * 
     * @param vec the x-component, y-component, and z-component
     * @param w   the w-component
     */
    public Vec4(Vec3 vec, float w) {
        x = vec.x;
        y = vec.y;
        z = vec.z;
        this.w = w;
    }

    /**
     * Returns a Vec3 containing the x-component, y-component, and z-component
     * (directly corresponding component) of this Vec4.
     * 
     * @return a Vec3 containing the first three components of the Vec4
     */
    public Vec3 xyz() {
        return new Vec3(x, y, z);
    }
}