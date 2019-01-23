package net.survival.block.message;

import net.survival.interaction.InteractionContext;

public abstract class BlockMessage
{
    public abstract void accept(BlockMessageVisitor visitor, InteractionContext ic);
}