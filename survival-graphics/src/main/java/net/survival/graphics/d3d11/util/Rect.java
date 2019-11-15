package net.survival.graphics.d3d11.util;

public final class Rect
{
    public final int x;
    public final int y;
    public final int width;
    public final int height;

    public Rect(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public int getLeft() {
        return x;
    }

    public int getRight() {
        return x + width;
    }

    public int getTop() {
        return y;
    }

    public int getBottom() {
        return y + height;
    }

    public static boolean intersects(Rect a, Rect b) {
        return (b.x + b.width >= a.x || b.x <= a.x + a.width) && (b.y + b.height >= a.y || b.x <= a.y + a.height);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Rect))
            return false;

        Rect r = (Rect) o;
        return x == r.x && y == r.y && width == r.width && height == r.height;
    }

    @Override
    public int hashCode() {
        int hash = 17;
        hash = 37 * hash + x;
        hash = 37 * hash + y;
        hash = 37 * hash + width;
        hash = 37 * hash + height;
        return hash;
    }

    @Override
    public String toString() {
        return "Rectangle[x=" + x + ",y=" + y + ",width=" + width + ",height=" + height + "]";
    }
}