package net.survival.util;

public class HitBox
{
    public final double radiusX;
    public final double radiusY;
    public final double radiusZ;

    public HitBox(double radiusX, double radiusY, double radiusZ) {
        this.radiusX = radiusX;
        this.radiusY = radiusY;
        this.radiusZ = radiusZ;
    }

    public static boolean intersects(double x1, double y1, double z1, HitBox hitBox1, double x2,
            double y2, double z2, HitBox hitBox2)
    {
        if (hitBox1.getRight(x1) < hitBox2.getLeft(x2))
            return false;
        if (hitBox1.getLeft(x1) > hitBox2.getRight(x2))
            return false;
        if (hitBox1.getTop(y1) < hitBox2.getBottom(y2))
            return false;
        if (hitBox1.getBottom(y1) > hitBox2.getTop(y2))
            return false;
        if (hitBox1.getFront(z1) < hitBox2.getBack(z2))
            return false;
        if (hitBox1.getBack(z1) > hitBox2.getFront(z2))
            return false;

        return true;
    }

    public double getTop(double offsetY) {
        return offsetY + radiusY;
    }

    public double getBottom(double offsetY) {
        return offsetY - radiusY;
    }

    public double getLeft(double offsetX) {
        return offsetX - radiusX;
    }

    public double getRight(double offsetX) {
        return offsetX + radiusX;
    }

    public double getFront(double offsetZ) {
        return offsetZ + radiusZ;
    }

    public double getBack(double offsetZ) {
        return offsetZ - radiusZ;
    }
}