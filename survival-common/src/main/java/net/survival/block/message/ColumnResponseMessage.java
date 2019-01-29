package net.survival.block.message;

import net.survival.block.column.ColumnResponse;
import net.survival.interaction.InteractionContext;

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

    public ColumnResponse getColumnResponse() {
        return columnResponse;
    }
}