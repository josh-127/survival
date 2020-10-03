package net.survival.block;

public interface ColumnProvider {
    Column provideColumn(long hashedPos);

    default Column provideColumn(int cx, int cz) {
        return provideColumn(ColumnPos.hashPos(cx, cz));
    }
}