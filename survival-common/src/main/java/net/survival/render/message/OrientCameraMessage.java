package net.survival.render.message;

import net.survival.interaction.InteractionContext;
import net.survival.interaction.MessagePriority;

public class OrientCameraMessage extends RenderMessage
{
    private final float yaw;
    private final float pitch;
    private final float roll;

    public OrientCameraMessage(float yaw, float pitch, float roll) {
        this.yaw = yaw;
        this.pitch = pitch;
        this.roll = roll;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public float getRoll() {
        return roll;
    }

    @Override
    public void accept(RenderMessageVisitor visitor, InteractionContext ic) {
        visitor.visit(ic, this);
    }

    @Override
    public int getPriority() {
        return MessagePriority.RESERVED_PRE_DRAW;
    }
}