package net.survival.block.message;

import net.survival.block.column.ColumnStageMask;
import net.survival.interaction.InteractionContext;

public class MaskColumnsMessage extends BlockMessage
{
    private final ColumnStageMask mask;

    public MaskColumnsMessage(ColumnStageMask mask) {
        this.mask = mask;
    }

    @Override
    public void accept(BlockMessageVisitor visitor, InteractionContext ic) {
        visitor.visit(ic, this);
    }

    public ColumnStageMask getMask() {
        return mask;
    }
}