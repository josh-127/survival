package net.survival.block.message;

public interface ColumnRequestVisitor
{
    void visit(CloseColumnRequest request);
    void visit(GetColumnRequest request);
    void visit(PostColumnRequest request);
}