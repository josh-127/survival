package net.survival.entity;

public abstract class Entity
{
    protected double x;
    protected double y;
    protected double z;
    protected double velocityX;
    protected double velocityY;
    protected double velocityZ;
    
    public Entity() {
    }
    
    public double getX() {
        return x;
    }
    
    public double getY() {
        return y;
    }
    
    public double getZ() {
        return z;
    }
    
    public void moveTo(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public double getVelocityX() {
        return velocityX;
    }
    
    public double getVelocityY() {
        return velocityY;
    }
    
    public double getVelocityZ() {
        return velocityZ;
    }
    
    public void setVelocity(double vx, double vy, double vz) {
        velocityX = vx;
        velocityY = vy;
        velocityZ = vz;
    }
}