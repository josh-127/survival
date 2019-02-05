package net.survival.block.message;

import net.survival.interaction.InteractionContext;
import net.survival.interaction.Message;
import net.survival.interaction.MessageVisitor;

public abstract class BlockMessage extends Message
{
    @Override
    public void accept(MessageVisitor visitor, InteractionContext ic) {
        visitor.visit(ic, this);
    }

    public abstract void accept(BlockMessageVisitor visitor, InteractionContext ic);
}