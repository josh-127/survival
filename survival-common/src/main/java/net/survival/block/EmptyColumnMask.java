package net.survival.block;

import java.util.Set;

public class EmptyColumnMask implements ColumnMask
{
    public static final EmptyColumnMask instance = new EmptyColumnMask();

    private EmptyColumnMask() {}

    @Override
    public Set<Long> getColumnPositions() {
        return Set.of();
    }
}