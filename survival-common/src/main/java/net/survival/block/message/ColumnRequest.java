package net.survival.block.message;

public abstract class ColumnRequest
{
    public abstract void accept(ColumnRequestVisitor visitor);
}