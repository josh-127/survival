package net.survival.block.message;

import net.survival.block.ColumnStageMask;
import net.survival.interaction.InteractionContext;
import net.survival.interaction.MessagePriority;

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
    
    @Override
    public int getPriority() {
        return MessagePriority.RESERVED_STEP;
    }

    public ColumnStageMask getMask() {
        return mask;
    }
}