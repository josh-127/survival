package net.survival.entity;

public abstract class Entity
{
    public double x;
    public double y;
    public double z;

    public Entity() {
        clearControlState();
    }

    public abstract void clearControlState();
}