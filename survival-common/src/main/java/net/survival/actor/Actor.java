package net.survival.actor;

import net.survival.util.HitBox;

public final class Actor {
    private static final double GRAVITY = 32.0;

    public double x;
    public double y;
    public double z;
    public double velX;
    public double velY;
    public double velZ;
    public HitBox hitBox = HitBox.DEFAULT;
    
    public double directionX;
    public double directionZ;
    public double movementSpeed = 1.0;

    public void move(double dx, double dz) {
        directionX = dx;
        directionZ = dz;
    }

    public void jump(double height) {
        velY = Math.sqrt(2.0 * GRAVITY * height);
    }
}