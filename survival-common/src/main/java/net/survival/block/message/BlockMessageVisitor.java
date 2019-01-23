package net.survival.block.message;

public interface BlockMessageVisitor
{
    void visit(BreakBlockMessage message);
    void visit(PlaceBlockMessage message);
}