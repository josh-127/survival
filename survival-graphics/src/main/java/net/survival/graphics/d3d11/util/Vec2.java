package net.survival.graphics.d3d11.util;

/**
 * Represents a two-dimensional mathematical vector.
 */
public final class Vec2
{
    public static final Vec2 ZERO = new Vec2(0.0f, 0.0f);
    public static final Vec2 ONE = new Vec2(1.0f, 1.0f);
    public static final Vec2 LEFT = new Vec2(-1.0f, 0.0f);
    public static final Vec2 RIGHT = new Vec2(1.0f, 0.0f);
    public static final Vec2 DOWN = new Vec2(0.0f, -1.0f);
    public static final Vec2 UP = new Vec2(0.0f, 1.0f);

    public final float x;
    public final float y;

    /**
     * Constructs a Vec2 from the given components
     * 
     * @param x the x-component
     * @param y the y-component
     */
    public Vec2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns a swizzled version of this Vec2 as <x, y>.
     * 
     * @return a swizzled version of this Vec2 as <x, y>
     */
    public Vec2 xy() {
        return this;
    }

    /**
     * Returns a new vector with a different x-component.
     * 
     * @param x the new x-component
     * @return a new vector with the x-component equal to x
     */
    public Vec2 withX(float x) {
        return new Vec2(x, y);
    }

    /**
     * Returns a new vector with a different y-component.
     * 
     * @param y the new y-component
     * @return a new vector with the y-component equal to y
     */
    public Vec2 withY(float y) {
        return new Vec2(x, y);
    }

    /**
     * Returns the magnitude of the Vec2.
     * 
     * @return the magnitude of the Vec2
     */
    public float magnitude() {
        return MathF.sqrtf(x * x + y * y);
    }

    /**
     * Returns the square magnitude of the Vec2
     * 
     * @return the square magnitude of the Vec2
     */
    public float sqrMagnitude() {
        return x * x + y * y;
    }

    /**
     * Returns a normalized version of the Vec2
     * 
     * @return a normalized version of the Vec2
     */
    public Vec2 normalized() {
        float inverseMagnitude = 1.0f / magnitude();
        return new Vec2(x * inverseMagnitude, y * inverseMagnitude);
    }

    /**
     * Returns the dot product between two vectors.
     * 
     * @param lhs the left operand
     * @param rhs the right operand
     * @return the dot product between lhs and rhs
     */
    public static float dot(Vec2 lhs, Vec2 rhs) {
        return lhs.x * rhs.x + lhs.y * rhs.y;
    }

    /**
     * Returns the linear interpolation between two vectors.
     * 
     * @param a the first point
     * @param b the second point
     * @param t the position within the infinite line formed by a and b
     * @return a linear interpolated vector between the infinite line formed by a
     *         and b
     */
    public static Vec2 lerp(Vec2 a, Vec2 b, float t) {
        return add(a, mul(sub(b, a), t));
    }

    /**
     * Returns a rotated version of a Vec2 around the z-axis by an angle.
     * 
     * @param vec   the vector to rotate
     * @param angle the angle to rotate in radians
     * @return a rotated version of vec around the z-axis by angle
     */
    public static Vec2 rotate(Vec2 vec, float angle) {
        return new Vec2(
                vec.x * MathF.cosf(angle) - vec.y * MathF.sinf(angle),
                vec.y * MathF.cosf(angle) + vec.x * MathF.sinf(angle));
    }

    /**
     * Returns a negative version of a Vec2.
     * 
     * @param vec the vector to negate
     * @return a negative version of vec
     */
    public static Vec2 neg(Vec2 vec) {
        return new Vec2(-vec.x, -vec.y);
    }

    /**
     * Adds two vectors together.
     * 
     * @param lhs the left operand
     * @param rhs the right operand
     * @return the sum of lhs + rhs
     */
    public static Vec2 add(Vec2 lhs, Vec2 rhs) {
        return new Vec2(lhs.x + rhs.x, lhs.y + rhs.y);
    }

    /**
     * Subtracts two vectors together.
     * 
     * @param lhs the left operand
     * @param rhs the right operand
     * @return the difference of lhs - rhs
     */
    public static Vec2 sub(Vec2 lhs, Vec2 rhs) {
        return new Vec2(lhs.x - rhs.x, lhs.y - rhs.y);
    }

    /**
     * Multiplies the components of two vectors.
     * 
     * @param lhs the left operand
     * @param rhs the right operand
     * @return the product of <lhs.x * rhs.x, lhs.y * rhs.y>
     */
    public static Vec2 mul(Vec2 lhs, Vec2 rhs) {
        return new Vec2(lhs.x * rhs.x, lhs.y * rhs.y);
    }

    /**
     * Divides the component of two vectors.
     * 
     * @param lhs the left operand
     * @param rhs the right operand
     * @return the quotient of <lhs.x / rhs.x, lhs.y / rhs.y>
     */
    public static Vec2 div(Vec2 lhs, Vec2 rhs) {
        return new Vec2(lhs.x / rhs.x, lhs.y / rhs.y);
    }

    /**
     * Multiplies a vector by a scalar.
     * 
     * @param lhs the left operand
     * @param rhs the right operand
     * @return the product of lhs * rhs
     */
    public static Vec2 mul(Vec2 lhs, float rhs) {
        return new Vec2(lhs.x * rhs, lhs.y * rhs);
    }

    /**
     * Divides a vector by a scalar.
     * 
     * @param lhs the left operand
     * @param rhs the right operand
     * @return the quotient of lhs / rhs
     */
    public static Vec2 div(Vec2 lhs, float rhs) {
        return new Vec2(lhs.x / rhs, lhs.y / rhs);
    }

    /**
     * Multiplies a scalar by a vector.
     * 
     * @param lhs the left operand
     * @param rhs the right operand
     * @return the product of lhs * rhs
     */
    public static Vec2 mul(float lhs, Vec2 rhs) {
        return new Vec2(lhs * rhs.x, lhs * rhs.y);
    }

    /**
     * Divides a scalar by a vector,
     * 
     * @param lhs the left operand
     * @param rhs the right operand
     * @return quotient of lhs / rhs
     */
    public static Vec2 div(float lhs, Vec2 rhs) {
        return new Vec2(lhs / rhs.x, lhs / rhs.y);
    }

    /**
     * Checks if the Vec2 is equal to another Vec2.
     * 
     * @param o the Vec2 to compare
     * @return true if o is a Vec2 and is equal to this Vec2; otherwise false
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Vec2))
            return false;

        Vec2 v = (Vec2) o;
        return x == v.x && y == v.y;
    }

    /**
     * Returns a string representation of the Vec2.
     * 
     * @return a string representation of the Vec2
     */
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}