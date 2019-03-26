package net.survival.render.message;

import net.survival.interaction.InteractionContext;
import net.survival.interaction.MessagePriority;
import net.survival.render.ModelType;

public class DrawModelMessage extends RenderMessage
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

    private DrawModelMessage(
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

    @Override
    public void accept(RenderMessageVisitor visitor, InteractionContext ic) {
        visitor.visit(ic, this);
    }

    @Override
    public int getPriority() {
        return MessagePriority.RESERVED_DRAW.getValue();
    }

    public static class Builder
    {
        private double x;
        private double y;
        private double z;
        private double yaw;
        private double pitch;
        private double roll;
        private double scaleX = 1.0;
        private double scaleY = 1.0;
        private double scaleZ = 1.0;
        private ModelType modelType = ModelType.HUMAN;

        public DrawModelMessage build() {
            return new DrawModelMessage(
                    x, y, z,
                    yaw, pitch, roll,
                    scaleX, scaleY, scaleZ,
                    modelType);
        }

        public Builder withPosition(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
            return this;
        }

        public Builder withRotation(double yaw, double pitch, double roll) {
            this.yaw = yaw;
            this.pitch = pitch;
            this.roll = roll;
            return this;
        }

        public Builder withScale(double sx, double sy, double sz) {
            scaleX = sx;
            scaleY = sy;
            scaleZ = sz;
            return this;
        }

        public Builder withModelType(ModelType as) {
            modelType = as;
            return this;
        }
    }
}