package net.survival.block.message;

import net.survival.interaction.InteractionContext;

public interface BlockMessageVisitor
{
    void visit(InteractionContext ic, BreakBlockMessage message);
    void visit(InteractionContext ic, PlaceBlockMessage message);

    void visit(InteractionContext ic, CheckInColumnsMessage message);
    void visit(InteractionContext ic, CheckOutColumnsMessage message);
    void visit(InteractionContext ic, ColumnResponseMessage message);
}