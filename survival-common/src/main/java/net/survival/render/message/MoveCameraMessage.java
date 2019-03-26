package net.survival.render.message;

import net.survival.interaction.InteractionContext;
import net.survival.interaction.MessagePriority;

public class MoveCameraMessage extends RenderMessage
{
    private final float x;
    private final float y;
    private final float z;

    public MoveCameraMessage(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    @Override
    public void accept(RenderMessageVisitor visitor, InteractionContext ic) {
        visitor.visit(ic, this);
    }

    @Override
    public int getPriority() {
        return MessagePriority.RESERVED_PRE_DRAW.getValue();
    }
}