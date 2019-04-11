package net.survival.gen.structure;

public abstract class Structure implements StructureMessageVisitor
{
    protected final int x;
    protected final int y;
    protected final int z;
    protected final int radius;

    public Structure(int x, int y, int z, int radius) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.radius = radius;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public int getRadius() {
        return radius;
    }
}