package net.survival.block.message;

import net.survival.interaction.InteractionContext;
import net.survival.interaction.MessagePriority;

public class BreakBlockMessage extends BlockMessage
{
    private final int x;
    private final int y;
    private final int z;

    public BreakBlockMessage(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    @Override
    public void accept(BlockMessageVisitor visitor, InteractionContext ic) {
        visitor.visit(ic, this);
    }

    @Override
    public int getPriority() {
        return MessagePriority.GAMEPLAY_POST_STEP.ordinal();
    }
}