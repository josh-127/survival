package net.survival.render.message;

import net.survival.interaction.InteractionContext;
import net.survival.interaction.MessagePriority;

public class SetCameraParamsMessage extends RenderMessage
{
    private final float fov;
    private final float width;
    private final float height;
    private final float near;
    private final float far;

    public SetCameraParamsMessage(float fov, float width, float height, float near, float far) {
        this.fov = fov;
        this.width = width;
        this.height = height;
        this.near = near;
        this.far = far;
    }

    public float getFov() {
        return fov;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public float getNearClipPlane() {
        return near;
    }

    public float getFarClipPlane() {
        return far;
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