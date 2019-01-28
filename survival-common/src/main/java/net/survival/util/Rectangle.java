package net.survival.util;

public class Rectangle
{
    private double left;
    private double top;
    private double right;
    private double bottom;

    public Rectangle() {}

    public Rectangle(double left, double top, double right, double bottom) {
        super();
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    public double getArea() {
        return Math.abs(right - left) * Math.abs(top - bottom);
    }

    public double getLeft() {
        return left;
    }

    public void setLeft(double to) {
        this.left = to;
    }

    public double getTop() {
        return top;
    }

    public void setTop(double to) {
        this.top = to;
    }

    public double getRight() {
        return right;
    }

    public void setRight(double to) {
        this.right = to;
    }

    public double getBottom() {
        return bottom;
    }

    public void setBottom(double to) {
        this.bottom = to;
    }

    @Override
    public int hashCode() {
        final var prime = 31;
        var result = 1;
        long temp;
        temp = Double.doubleToLongBits(bottom);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(left);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(right);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(top);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        Rectangle other = (Rectangle) obj;

        if (left != other.left)
            return false;
        if (top != other.top)
            return false;
        if (right != other.right)
            return false;
        if (bottom != other.bottom)
            return false;

        return true;
    }

    @Override
    public String toString() {
        return "Rectangle [left=" + left + ", top=" + top + ", right=" + right + ", bottom=" + bottom + "]";
    }
}