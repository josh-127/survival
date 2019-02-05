package net.survival.block.message;

import net.survival.interaction.InteractionContext;
import net.survival.interaction.MessagePriority;

public class PlaceBlockMessage extends BlockMessage
{
    private final int x;
    private final int y;
    private final int z;
    private final int fullID;

    public PlaceBlockMessage(int x, int y, int z, int fullID) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.fullID = fullID;
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

    public int getFullID() {
        return fullID;
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