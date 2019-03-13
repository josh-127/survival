package net.survival.client.util;

/**
 * Represents a three-dimensional vector
 */
public final class Vec3
{
    public static final Vec3 ZERO = new Vec3(0.0f, 0.0f, 0.0f);
    public static final Vec3 ONE = new Vec3(1.0f, 1.0f, 1.0f);
    public static final Vec3 DOWN = new Vec3(0.0f, 0.0f, -1.0f);
    public static final Vec3 UP = new Vec3(0.0f, 0.0f, 1.0f);
    public static final Vec3 LEFT = new Vec3(-1.0f, 0.0f, 0.0f);
    public static final Vec3 RIGHT = new Vec3(1.0f, 0.0f, 0.0f);
    public static final Vec3 BACK = new Vec3(0.0f, -1.0f, 0.0f);
    public static final Vec3 FORWARD = new Vec3(0.0f, 1.0f, 0.0f);

    public final float x;
    public final float y;
    public final float z;

    /**
     * Constructs a Vec3.
     * 
     * @param x the x-component
     * @param y the y-component
     * @param z the z-component
     */
    public Vec3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Constructs a Vec3.
     * 
     * @param vec the x-component and y-component
     * @param z   the z-component
     */
    public Vec3(Vec2 vec, float z) {
        x = vec.x;
        y = vec.y;
        this.z = z;
    }

    /**
     * Returns the x-component and y-component of the Vec3.
     * 
     * @return the x-component and y-component of the Vec3
     */
    public Vec2 xy() {
        return new Vec2(x, y);
    }

    /**
     * Returns a Vec3 with a different x-component.
     * 
     * @param x the new x-component
     * @return a Vec3 with a different x-component
     */
    public Vec3 withX(float x) {
        return new Vec3(x, y, z);
    }

    /**
     * Returns a Vec3 with a different y-component.
     * 
     * @param x the new y-component
     * @return a Vec3 with a different y-component
     */
    public Vec3 withY(float y) {
        return new Vec3(x, y, z);
    }

    /**
     * Returns a Vec3 with a different y-component.
     * 
     * @param x the new y-component
     * @return a Vec3 with a different y-component
     */
    public Vec3 withZ(float z) {
        return new Vec3(x, y, z);
    }

    /**
     * Returns a Vec3 with a different x-component and y-component.
     * 
     * @param xy the new x-component and y-component
     * @return a Vec3 with a different x-component and y-component
     */
    public Vec3 withXY(Vec2 xy) {
        return new Vec3(xy.x, xy.y, z);
    }

    /**
     * Returns the magnitude of the Vec3.
     * 
     * @return the magnitude of the Vec3
     */
    public float magnitude() {
        return MathF.sqrtf(x * x + y * y + z * z);
    }

    /**
     * Returns the square magnitude of the Vec3.
     * 
     * @return the square magnitude of the Vec3
     */
    public float sqrMagnitude() {
        return x * x + y * y + z * z;
    }

    /**
     * Returns a normalized version of the Vec3.
     * 
     * @return a normalized version of the Vec3
     */
    public Vec3 normalized() {
        float inverseMagnitude = 1.0f / magnitude();
        return new Vec3(x * inverseMagnitude, y * inverseMagnitude, z * inverseMagnitude);
    }

    /**
     * Returns the dot product of two vectors.
     * 
     * @param lhs the left operand
     * @param rhs the right operand
     * @return the dot product of lhs and rhs
     */
    public static float dot(Vec3 lhs, Vec3 rhs) {
        return lhs.x * rhs.x + lhs.y * rhs.y + lhs.z * rhs.z;
    }

    /**
     * Returns the cross product of two vectors.
     * 
     * @param lhs the left operand
     * @param rhs the right operand
     * @return the cross product of lhs and rhs
     */
    public static Vec3 cross(Vec3 lhs, Vec3 rhs) {
        return new Vec3(lhs.y * rhs.z - lhs.z * rhs.y, lhs.z * rhs.x - lhs.x * rhs.z, lhs.x * rhs.y - lhs.y * rhs.x);
    }

    /**
     * Returns a linear interpolation between two vectors
     * 
     * @param a the first vector
     * @param b the second vector
     * @param t the position between the infinite line formed by a and b
     * @return a linear interpolation between a and b
     */
    public static Vec3 lerp(Vec3 a, Vec3 b, float t) {
        return add(a, mul(sub(b, a), t));
    }

    /**
     * Returns the scale of two vectors.
     * 
     * @param lhs the vector to be scaled
     * @param rhs the scale
     * @return the scale of two vectors
     */
    public static Vec3 scale(Vec3 lhs, Vec3 rhs) {
        return new Vec3(lhs.x * rhs.x, lhs.y * rhs.y, lhs.z * rhs.z);
    }

    /**
     * Returns the inverse scale of two vectors.
     * 
     * @param lhs the vector to be scaled
     * @param rhs the inverse scale
     * @return the scale of two vectors
     */
    public static Vec3 invScale(Vec3 lhs, Vec3 rhs) {
        return new Vec3(lhs.x / rhs.x, lhs.y / rhs.y, lhs.z / rhs.z);
    }

    /**
     * Returns a negative version of a Vec3.
     * 
     * @param vec the vector
     * @return a negative version of vec
     */
    public static Vec3 neg(Vec3 vec) {
        return new Vec3(-vec.x, -vec.y, -vec.z);
    }

    /**
     * Adds two vectors together.
     * 
     * @param lhs the left operand
     * @param rhs the right operand
     * @return the sum of lhs + rhs
     */
    public static Vec3 add(Vec3 lhs, Vec3 rhs) {
        return new Vec3(lhs.x + rhs.x, lhs.y + rhs.y, lhs.z + rhs.z);
    }

    /**
     * Subtracts two vectors together.
     * 
     * @param lhs the left operand
     * @param rhs the right operand
     * @return the different of lhs - rhs
     */
    public static Vec3 sub(Vec3 lhs, Vec3 rhs) {
        return new Vec3(lhs.x - rhs.x, lhs.y - rhs.y, lhs.z - rhs.z);
    }

    /**
     * Multiplies a vector by a scalar.
     * 
     * @param lhs the left operand
     * @param rhs the right operand
     * @return the product of lhs * rhs
     */
    public static Vec3 mul(Vec3 lhs, float rhs) {
        return new Vec3(lhs.x * rhs, lhs.y * rhs, lhs.z * rhs);
    }

    /**
     * Divides a vector by a scalar.
     * 
     * @param lhs the left operand
     * @param rhs the right operand
     * @return the quotient of lhs / rhs
     */
    public static Vec3 div(Vec3 lhs, float rhs) {
        return new Vec3(lhs.x / rhs, lhs.y / rhs, lhs.z / rhs);
    }

    /**
     * Multiplies a scalar by a vector.
     * 
     * @param lhs the left operand
     * @param rhs the right operand
     * @return the product of lhs * rhs
     */
    public static Vec3 mul(float lhs, Vec3 rhs) {
        return new Vec3(lhs * rhs.x, lhs * rhs.y, lhs * rhs.z);
    }

    /**
     * Divides a scalar by a vector.
     * 
     * @param lhs the left operand
     * @param rhs the right operand
     * @return the quotient of lhs / rhs
     */
    public static Vec3 div(float lhs, Vec3 rhs) {
        return new Vec3(lhs / rhs.x, lhs / rhs.y, lhs / rhs.z);
    }

    /**
     * Checks of the Vec3 is equal to another Vec3.
     * 
     * @param o the Vec3 to compare to
     * @return true if o is a Vec2 and is equal to this Vec3; otherwise false
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Vec3))
            return false;

        Vec3 v = (Vec3) o;
        return x == v.x && y == v.y && z == v.z;
    }

    /**
     * Returns a string representation of the Vec3.
     * 
     * @return a string representation of the Vec3
     */
    @Override
    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }
}