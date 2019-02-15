package net.survival.render.message;

import net.survival.interaction.InteractionContext;
import net.survival.interaction.MessagePriority;

public class SetSkyColorMessage extends RenderMessage
{
    private final float bottomR;
    private final float bottomG;
    private final float bottomB;
    private final float topR;
    private final float topG;
    private final float topB;

    public SetSkyColorMessage(float br, float bg, float bb, float tr, float tg, float tb) {
        bottomR = br;
        bottomG = bg;
        bottomB = bb;
        topR = tr;
        topG = tg;
        topB = tb;
    }

    public float getBottomR() {
        return bottomR;
    }

    public float getBottomG() {
        return bottomG;
    }

    public float getBottomB() {
        return bottomB;
    }

    public float getTopR() {
        return topR;
    }

    public float getTopG() {
        return topG;
    }

    public float getTopB() {
        return topB;
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