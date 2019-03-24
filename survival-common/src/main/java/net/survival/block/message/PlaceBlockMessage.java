package net.survival.block.message;

import net.survival.interaction.InteractionContext;
import net.survival.interaction.MessagePriority;

public class PlaceBlockMessage extends BlockMessage
{
    private final int x;
    private final int y;
    private final int z;
    private final int fullId;

    public PlaceBlockMessage(int x, int y, int z, int fullId) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.fullId = fullId;
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

    public int getFullId() {
        return fullId;
    }

    @Override
    public void accept(BlockMessageVisitor visitor, InteractionContext ic) {
        visitor.visit(ic, this);
    }

    @Override
    public int getPriority() {
        return MessagePriority.GAMEPLAY_POST_STEP;
    }
}