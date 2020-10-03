package net.survival.graphics;

public class BitmapRegion {
    private int left;
    private int top;
    private int right;
    private int bottom;
    private Bitmap bitmap;

    public BitmapRegion(int left, int top, int right, int bottom, Bitmap bitmap) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.bitmap = bitmap;
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getRight() {
        return right;
    }

    public void setRight(int right) {
        this.right = right;
    }

    public int getBottom() {
        return bottom;
    }

    public void setBottom(int bottom) {
        this.bottom = bottom;
    }

    public int getWidth() {
        return right - left;
    }

    public int getHeight() {
        return bottom - top;
    }

    public int getArea() {
        return getWidth() * getHeight();
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    public int hashCode() {
        final var prime = 31;
        var result = 1;
        result = prime * result + bottom;
        result = prime * result + left;
        result = prime * result + right;
        result = prime * result + top;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        var other = (BitmapRegion) obj;

        if (bottom != other.bottom) {
            return false;
        }
        if (left != other.left) {
            return false;
        }
        if (right != other.right) {
            return false;
        }
        if (top != other.top) {
            return false;
        }

        return true;
    }
}