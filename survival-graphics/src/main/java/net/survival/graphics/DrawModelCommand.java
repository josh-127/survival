package net.survival.graphics;

import net.survival.render.ModelType;

final class DrawModelCommand extends DrawCommand
{
    private final double x;
    private final double y;
    private final double z;
    private final double yaw;
    private final double pitch;
    private final double roll;
    private final double scaleX;
    private final double scaleY;
    private final double scaleZ;
    private final ModelType modelType;

    public DrawModelCommand(
            double x, double y, double z,
            double yaw, double pitch, double roll,
            double scaleX, double scaleY, double scaleZ,
            ModelType modelType)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.roll = roll;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.scaleZ = scaleZ;
        this.modelType = modelType;
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

    public ModelType getModelType() {
        return modelType;
    }
}