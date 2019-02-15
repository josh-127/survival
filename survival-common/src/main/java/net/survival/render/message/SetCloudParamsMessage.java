package net.survival.render.message;

import net.survival.interaction.InteractionContext;
import net.survival.interaction.MessagePriority;

public class SetCloudParamsMessage extends RenderMessage
{
    private final long seed;
    private final float density;
    private final float elevation;
    private final float speedX;
    private final float speedZ;
    private final float alpha;

    public SetCloudParamsMessage(
            long seed,
            float density,
            float elevation,
            float speedX,
            float speedZ,
            float alpha)
    {
        this.seed = seed;
        this.density = density;
        this.elevation = elevation;
        this.speedX = speedX;
        this.speedZ = speedZ;
        this.alpha = alpha;
    }

    public long getSeed() {
        return seed;
    }

    public float getDensity() {
        return density;
    }

    public float getElevation() {
        return elevation;
    }

    public float getSpeedX() {
        return speedX;
    }

    public float getSpeedZ() {
        return speedZ;
    }

    public float getAlpha() {
        return alpha;
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