package net.survival.entity;

public class Entity
{
    public double x;
    public double y;
    public double z;
    public double velocityX;
    public double velocityY;
    public double velocityZ;
    
    public double collisionBoxRadiusX;
    public double collisionBoxRadiusY;
    public double collisionBoxRadiusZ;
    
    public Entity() {
        collisionBoxRadiusX = 0.4375;
        collisionBoxRadiusY = 0.4375;
        collisionBoxRadiusZ = 0.4375;
    }
}