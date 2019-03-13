package net.survival.client.util;

import static net.survival.client.util.MathF.cosf;
import static net.survival.client.util.MathF.sinf;
import static net.survival.client.util.MathF.tanf;

/**
 * Represents a four-by-four row-major matrix.
 * 
 * NOTE: There is no underlying array in order to increase code readability and
 * to reduce the overhead of dereferencing pointers because Mat4 may be used in
 * hot areas of code.
 */
public final class Mat4
{
    public static final Mat4 IDENTITY = new Mat4(
            1.0f, 0.0f, 0.0f, 0.0f,
            0.0f, 1.0f, 0.0f, 0.0f,
            0.0f, 0.0f, 1.0f, 0.0f,
            0.0f, 0.0f, 0.0f, 1.0f);

    public final float m11, m12, m13, m14;
    public final float m21, m22, m23, m24;
    public final float m31, m32, m33, m34;
    public final float m41, m42, m43, m44;

    /**
     * Constructs a Mat4.
     * 
     * @param m11 row 1, column 1
     * @param m12 row 1, column 2
     * @param m13 row 1, column 3
     * @param m14 row 1, column 4
     * @param m21 row 2, column 1
     * @param m22 row 2, column 2
     * @param m23 row 2, column 3
     * @param m24 row 2, column 4
     * @param m31 row 3, column 1
     * @param m32 row 3, column 2
     * @param m33 row 3, column 3
     * @param m34 row 3, column 4
     * @param m41 row 4, column 1
     * @param m42 row 4, column 2
     * @param m43 row 4, column 3
     * @param m44 row 4, column 4
     */
    public Mat4(
            float m11, float m12, float m13, float m14,
            float m21, float m22, float m23, float m24,
            float m31, float m32, float m33, float m34,
            float m41, float m42, float m43, float m44)
    {
        this.m11 = m11;
        this.m12 = m12;
        this.m13 = m13;
        this.m14 = m14;
        this.m21 = m21;
        this.m22 = m22;
        this.m23 = m23;
        this.m24 = m24;
        this.m31 = m31;
        this.m32 = m32;
        this.m33 = m33;
        this.m34 = m34;
        this.m41 = m41;
        this.m42 = m42;
        this.m43 = m43;
        this.m44 = m44;
    }

    /**
     * Multiplies two matrices together.
     * 
     * @param lhs the left operand
     * @param rhs the right operand
     * @return the product of lhs * rhs
     */
    public static Mat4 mul(Mat4 lhs, Mat4 rhs) {
        return new Mat4(
                lhs.m11 * rhs.m11 + lhs.m12 * rhs.m21 + lhs.m13 * rhs.m31 + lhs.m14 * rhs.m41,
                lhs.m11 * rhs.m12 + lhs.m12 * rhs.m22 + lhs.m13 * rhs.m32 + lhs.m14 * rhs.m42,
                lhs.m11 * rhs.m13 + lhs.m12 * rhs.m23 + lhs.m13 * rhs.m33 + lhs.m14 * rhs.m43,
                lhs.m11 * rhs.m14 + lhs.m12 * rhs.m24 + lhs.m13 * rhs.m34 + lhs.m14 * rhs.m44,

                lhs.m21 * rhs.m11 + lhs.m22 * rhs.m21 + lhs.m23 * rhs.m31 + lhs.m24 * rhs.m41,
                lhs.m21 * rhs.m12 + lhs.m22 * rhs.m22 + lhs.m23 * rhs.m32 + lhs.m24 * rhs.m42,
                lhs.m21 * rhs.m13 + lhs.m22 * rhs.m23 + lhs.m23 * rhs.m33 + lhs.m24 * rhs.m43,
                lhs.m21 * rhs.m14 + lhs.m22 * rhs.m24 + lhs.m23 * rhs.m34 + lhs.m24 * rhs.m44,

                lhs.m31 * rhs.m11 + lhs.m32 * rhs.m21 + lhs.m33 * rhs.m31 + lhs.m34 * rhs.m41,
                lhs.m31 * rhs.m12 + lhs.m32 * rhs.m22 + lhs.m33 * rhs.m32 + lhs.m34 * rhs.m42,
                lhs.m31 * rhs.m13 + lhs.m32 * rhs.m23 + lhs.m33 * rhs.m33 + lhs.m34 * rhs.m43,
                lhs.m31 * rhs.m14 + lhs.m32 * rhs.m24 + lhs.m33 * rhs.m34 + lhs.m34 * rhs.m44,

                lhs.m41 * rhs.m11 + lhs.m42 * rhs.m21 + lhs.m43 * rhs.m31 + lhs.m44 * rhs.m41,
                lhs.m41 * rhs.m12 + lhs.m42 * rhs.m22 + lhs.m43 * rhs.m32 + lhs.m44 * rhs.m42,
                lhs.m41 * rhs.m13 + lhs.m42 * rhs.m23 + lhs.m43 * rhs.m33 + lhs.m44 * rhs.m43,
                lhs.m41 * rhs.m14 + lhs.m42 * rhs.m24 + lhs.m43 * rhs.m34 + lhs.m44 * rhs.m44);
    }

    /**
     * Multiplies a vector by a matrix.
     * 
     * @param lhs the left operand
     * @param rhs the right operand
     * @return the product of lhs * rhs
     */
    public static Vec4 mul(Vec4 lhs, Mat4 rhs) {
        return new Vec4(
                lhs.x * rhs.m11 + lhs.y * rhs.m21 + lhs.z * rhs.m31 + lhs.w * rhs.m41,
                lhs.x * rhs.m12 + lhs.y * rhs.m22 + lhs.z * rhs.m32 + lhs.w * rhs.m42,
                lhs.x * rhs.m13 + lhs.y * rhs.m23 + lhs.z * rhs.m33 + lhs.w * rhs.m43,
                lhs.x * rhs.m14 + lhs.y * rhs.m24 + lhs.z * rhs.m34 + lhs.w * rhs.m44);
    }

    /**
     * Returns an inverse matrix of a given matrix.
     * 
     * @param mat the matrix to inverse
     * @return an inverse matrix of mat
     */
    public static Mat4 inverse(Mat4 mat) {
        float inverseDet = 1.0f / (mat.m11
                * (mat.m22 * mat.m33 * mat.m44 + mat.m23 * mat.m34 * mat.m42 + mat.m24 * mat.m32 * mat.m43)
                + mat.m12 * (mat.m21 * mat.m34 * mat.m43 + mat.m23 * mat.m31 * mat.m44 + mat.m24 * mat.m33 * mat.m41)
                + mat.m13 * (mat.m21 * mat.m32 * mat.m44 + mat.m22 * mat.m34 * mat.m41 + mat.m24 * mat.m31 * mat.m42)
                + mat.m14 * (mat.m21 * mat.m33 * mat.m42 + mat.m22 * mat.m31 * mat.m43 + mat.m23 * mat.m32 * mat.m41)
                - mat.m11 * (mat.m22 * mat.m34 * mat.m43 + mat.m23 * mat.m32 * mat.m44 + mat.m24 * mat.m33 * mat.m42)
                - mat.m12 * (mat.m21 * mat.m33 * mat.m44 + mat.m23 * mat.m34 * mat.m41 + mat.m24 * mat.m31 * mat.m43)
                - mat.m13 * (mat.m21 * mat.m34 * mat.m42 + mat.m22 * mat.m31 * mat.m44 + mat.m24 * mat.m32 * mat.m41)
                - mat.m14 * (mat.m21 * mat.m32 * mat.m43 + mat.m22 * mat.m33 * mat.m41 + mat.m23 * mat.m31 * mat.m42));

        return new Mat4(
                inverseDet * mat.m11, inverseDet * mat.m21, inverseDet * mat.m31, inverseDet * mat.m41,
                inverseDet * mat.m12, inverseDet * mat.m22, inverseDet * mat.m32, inverseDet * mat.m42,
                inverseDet * mat.m13, inverseDet * mat.m23, inverseDet * mat.m33, inverseDet * mat.m43,
                inverseDet * mat.m14, inverseDet * mat.m24, inverseDet * mat.m34, inverseDet * mat.m44);
    }

    /**
     * Returns a transposed matrix of a given matrix.
     * 
     * @param mat the matrix to transpose
     * @return a transposed matrix of mat
     */
    public static Mat4 transpose(Mat4 mat) {
        return new Mat4(
                mat.m11, mat.m21, mat.m31, mat.m41,
                mat.m12, mat.m22, mat.m32, mat.m42,
                mat.m13, mat.m23, mat.m33, mat.m43,
                mat.m14, mat.m24, mat.m34, mat.m44);
    }

    /**
     * Returns a translation matrix
     * 
     * @param translation the translation
     * @return a translation matrix
     */
    public static Mat4 translate(Vec3 translation) {
        return new Mat4(
                1.0f, 0.0f, 0.0f, 0.0f,
                0.0f, 1.0f, 0.0f, 0.0f,
                0.0f, 0.0f, 1.0f, 0.0f,
                translation.x, translation.y, translation.z, 1.0f);
    }

    /**
     * Returns an x-axis angle rotation matrix.
     * 
     * @param angle the angle to rotate in radians
     * @return an x-axis angle rotation matrix
     */
    public static Mat4 rotateX(float angle) {
        return new Mat4(
                1.0f, 0.0f, 0.0f, 0.0f,
                0.0f, cosf(angle), -sinf(angle), 0.0f,
                0.0f, sinf(angle), cosf(angle), 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f);
    }

    /**
     * Returns an y-axis angle rotation matrix.
     * 
     * @param angle the angle to rotate in radians
     * @return an y-axis angle rotation matrix
     */
    public static Mat4 rotateY(float angle) {
        return new Mat4(
                cosf(angle), 0.0f, sinf(angle), 0.0f,
                0.0f, 1.0f, 0.0f, 0.0f,
                -sinf(angle), 0.0f, cosf(0.0f), 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f);
    }

    /**
     * Returns an z-axis angle rotation matrix.
     * 
     * @param angle the angle to rotate in radians
     * @return an z-axis angle rotation matrix
     */
    public static Mat4 rotateZ(float angle) {
        return new Mat4(
                cosf(angle), -sinf(angle), 0.0f, 0.0f,
                sinf(angle), cosf(angle), 0.0f, 0.0f,
                0.0f, 0.0f, 1.0f, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f);
    }

    /**
     * Returns a scale matrix.
     * 
     * @param scale the scale
     * @return a scale matrix
     */
    public static Mat4 scale(Vec3 scale) {
        return new Mat4(
                scale.x, 0.0f, 0.0f, 0.0f,
                0.0f, scale.y, 0.0f, 0.0f,
                0.0f, 0.0f, scale.z, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f);
    }

    /**
     * Returns a look-at view matrix.
     * 
     * @param position the position of the camera
     * @param target   the point the camera is looking at
     * @param up       the up-axis of the camera
     * @return a look-at view matrix
     */
    public static Mat4 lookAt(Vec3 position, Vec3 target, Vec3 up) {
        Vec3 axisZ = Vec3.sub(position, target).normalized();
        Vec3 axisX = Vec3.cross(up, axisZ).normalized();
        Vec3 axisY = Vec3.cross(axisZ, axisX);

        return new Mat4(
                axisX.x, axisY.x, axisZ.x, 0.0f,
                axisX.y, axisY.y, axisZ.y, 0.0f,
                axisX.z, axisY.z, axisZ.z, 0.0f,
                -Vec3.dot(axisX, position),
                -Vec3.dot(axisY, position),
                -Vec3.dot(axisZ, position),
                1.0f);
    }

    /**
     * Returns a perspective matrix.
     * 
     * @param fov         the field-of-view in radians
     * @param aspectRatio the aspect ratio
     * @param near        the z-near value
     * @param far         the z-far value
     * @return a perspective matrix
     */
    public static Mat4 perspective(float fov, float aspectRatio, float near, float far) {
        float m22 = 1.0f / tanf(0.5f * fov);
        float m11 = m22 / aspectRatio;
        float m33 = far / (near - far);
        float m43 = (near * far) / (near - far);

        return new Mat4(
                m11, 0.0f, 0.0f, 0.0f,
                0.0f, m22, 0.0f, 0.0f,
                0.0f, 0.0f, m33, -1.0f,
                0.0f, 0.0f, m43, 0.0f);
    }
}