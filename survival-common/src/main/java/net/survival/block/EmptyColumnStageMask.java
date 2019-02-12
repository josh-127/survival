package net.survival.block;

import java.util.Set;

public class EmptyColumnStageMask implements ColumnStageMask
{
    public static final EmptyColumnStageMask instance = new EmptyColumnStageMask();

    private EmptyColumnStageMask() {}

    @Override
    public Set<Long> getColumnPositions() {
        return Set.of();
    }
}