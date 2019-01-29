package net.survival.block.message;

import java.util.Set;

import net.survival.interaction.InteractionContext;

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

    public Set<Long> getColumnPositions() {
        return columnPositions;
    }
}