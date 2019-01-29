package net.survival.block.message;

import net.survival.interaction.InteractionContext;

public class CheckInColumnsMessage extends BlockMessage
{
    @Override
    public void accept(BlockMessageVisitor visitor, InteractionContext ic) {
        visitor.visit(ic, this);
    }
}