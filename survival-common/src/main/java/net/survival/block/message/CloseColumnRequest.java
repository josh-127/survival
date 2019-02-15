package net.survival.block.message;

public class CloseColumnRequest extends ColumnRequest
{
    @Override
    public void accept(ColumnRequestVisitor visitor) {
        visitor.visit(this);
    }
}