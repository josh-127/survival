package net.survival.block.message;

public abstract class BlockMessage
{
    public abstract void accept(BlockMessageVisitor visitor);
}