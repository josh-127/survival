package net.survival.block.message;

import net.survival.block.Column;
import net.survival.interaction.InteractionContext;
import net.survival.interaction.MessagePriority;

public class ColumnResponseMessage extends BlockMessage
{
    private final long columnPos;
    private final Column column;

    public ColumnResponseMessage(long columnPos, Column column) {
        this.columnPos = columnPos;
        this.column = column;
    }

    public long getColumnPos() {
        return columnPos;
    }

    public Column getColumn() {
        return column;
    }

    @Override
    public void accept(BlockMessageVisitor visitor, InteractionContext ic) {
        visitor.visit(ic, this);
    }

    @Override
    public int getPriority() {
        return MessagePriority.RESERVED_STEP.ordinal();
    }
}