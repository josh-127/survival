package net.survival.world.actor;

public abstract class Actor
{
    long containingChunk;

    protected double x;
    protected double y;
    protected double z;

    protected double yaw;
    protected double pitch;
    protected double roll;

    protected double scaleX;
    protected double scaleY;
    protected double scaleZ;

    protected ActorModel model = ActorModel.HUMAN;

    private boolean dead;

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public double getYaw() {
        return yaw;
    }

    public double getPitch() {
        return pitch;
    }

    public double getRoll() {
        return roll;
    }

    public double getScaleX() {
        return scaleX;
    }

    public double getScaleY() {
        return scaleY;
    }

    public double getScaleZ() {
        return scaleZ;
    }

    public ActorModel getModel() {
        return model;
    }

    public boolean isDead() {
        return dead;
    }

    protected void killSelf() {
        dead = true;
    }
}