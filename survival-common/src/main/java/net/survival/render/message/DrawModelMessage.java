package net.survival.render.message;

import net.survival.interaction.InteractionContext;
import net.survival.interaction.MessagePriority;
import net.survival.render.ModelType;

public class DrawModelMessage extends RenderMessage
{
    public final double x;
    public final double y;
    public final double z;
    public final double yaw;
    public final double pitch;
    public final double roll;
    public final double scaleX;
    public final double scaleY;
    public final double scaleZ;
    public final ModelType modelType;

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

    @Override
    public void accept(RenderMessageVisitor visitor, InteractionContext ic) {
        visitor.visit(ic, this);
    }

    @Override
    public int getPriority() {
        return MessagePriority.RESERVED_DRAW;
    }

    public ModelType getModelType() {
        return modelType;
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