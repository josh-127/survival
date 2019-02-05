package net.survival.block.message;

import net.survival.interaction.InteractionContext;
import net.survival.interaction.MessagePriority;

public class CheckInColumnsMessage extends BlockMessage
{
    @Override
    public void accept(BlockMessageVisitor visitor, InteractionContext ic) {
        visitor.visit(ic, this);
    }

    @Override
    public int getPriority() {
        return MessagePriority.RESERVED_PRE_STEP;
    }
}