package net.survival.block.message;

import net.survival.interaction.InteractionContext;

public interface BlockMessageVisitor
{
    void visit(InteractionContext ic, BreakBlockMessage message);
    void visit(InteractionContext ic, PlaceBlockMessage message);

    void visit(InteractionContext ic, ColumnResponseMessage message);
    void visit(InteractionContext ic, MaskColumnsMessage message);
}