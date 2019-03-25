package net.survival.block.message;

import net.survival.block.ColumnMask;
import net.survival.interaction.InteractionContext;
import net.survival.interaction.MessagePriority;

public class MaskColumnsMessage extends BlockMessage
{
    private final ColumnMask mask;

    public MaskColumnsMessage(ColumnMask mask) {
        this.mask = mask;
    }

    @Override
    public void accept(BlockMessageVisitor visitor, InteractionContext ic) {
        visitor.visit(ic, this);
    }
    
    @Override
    public int getPriority() {
        return MessagePriority.RESERVED_STEP.ordinal();
    }

    public ColumnMask getMask() {
        return mask;
    }
}