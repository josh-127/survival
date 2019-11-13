package net.survival.actor;

import net.survival.util.HitBox;

public abstract class Actor {
    private double x;
    private double y;
    private double z;
    private double velX;
    private double velY;
    private double velZ;
    private HitBox hitBox = HitBox.DEFAULT;
    
    private double directionX;
    private double directionZ;
    private double movementSpeed = 1.0;
    
    public double getX() { return x; }
    public double getY() { return y; }
    public double getZ() { return z; }
    public void setX(double to) { x = to; }
    public void setY(double to) { y = to; }
    public void setZ(double to) { z = to; }
    
    public double getVelX() { return velX; }
    public double getVelY() { return velY; }
    public double getVelZ() { return velZ; }
    public void setVelX(double to) { velX = to; }
    public void setVelY(double to) { velY = to; }
    public void setVelZ(double to) { velZ = to; }
    
    public HitBox getHitBox() { return hitBox; }
    public void setHitBox(HitBox to) { hitBox = to; }
    
    public double getDirectionX() { return directionX; }
    public double getDirectionZ() { return directionZ; }
    public double getMovementSpeed() { return movementSpeed; }
    public void setDirectionX(double to) { directionX = to; }
    public void setDirectionZ(double to) { directionZ = to; }
    public void setMovementSpeed(double to) { movementSpeed = to; }
}