package net.survival.block.message;

import java.util.Set;

import net.survival.interaction.InteractionContext;
import net.survival.interaction.MessagePriority;

public class CheckOutColumnsMessage extends BlockMessage
{
    private final Set<Long> columnPositions;

    public CheckOutColumnsMessage(Set<Long> columnPositions) {
        this.columnPositions = columnPositions;
    }

    @Override
    public void accept(BlockMessageVisitor visitor, InteractionContext ic) {
        visitor.visit(ic, this);
    }
    
    @Override
    public int getPriority() {
        return MessagePriority.RESERVED_PRE_STEP;
    }

    public Set<Long> getColumnPositions() {
        return columnPositions;
    }
}