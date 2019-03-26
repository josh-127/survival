package net.survival.render.message;

import net.survival.block.Column;
import net.survival.interaction.InteractionContext;
import net.survival.interaction.MessagePriority;

public class InvalidateColumnMessage extends RenderMessage
{
    public final long columnPos;
    public final Column column;
    public final ColumnInvalidationPriority invalidationPriority;

    public InvalidateColumnMessage(
            long columnPos,
            Column column,
            ColumnInvalidationPriority invalidationPriority)
    {
        this.columnPos = columnPos;
        this.column = column;
        this.invalidationPriority = invalidationPriority;
    }

    @Override
    public void accept(RenderMessageVisitor visitor, InteractionContext ic) {
        visitor.visit(ic, this);
    }

    @Override
    public int getPriority() {
        return MessagePriority.RESERVED_DRAW.getValue();
    }
}