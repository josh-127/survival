package net.survival.block.message;

import net.survival.block.ColumnResponse;
import net.survival.interaction.InteractionContext;
import net.survival.interaction.MessagePriority;

public class ColumnResponseMessage extends BlockMessage
{
    private final ColumnResponse columnResponse;

    public ColumnResponseMessage(ColumnResponse columnResponse) {
        this.columnResponse = columnResponse;
    }

    @Override
    public void accept(BlockMessageVisitor visitor, InteractionContext ic) {
        visitor.visit(ic, this);
    }
    
    @Override
    public int getPriority() {
        return MessagePriority.RESERVED_STEP;
    }

    public ColumnResponse getColumnResponse() {
        return columnResponse;
    }
}