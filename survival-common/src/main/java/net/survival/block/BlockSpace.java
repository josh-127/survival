package net.survival.block;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.survival.block.column.Column;
import net.survival.block.column.ColumnPos;

public class BlockSpace implements BlockStorage
{
    private final Long2ObjectOpenHashMap<Column> columns;

    public BlockSpace() {
        columns = new Long2ObjectOpenHashMap<>(1024);
    }

    public Column getColumn(int cx, int cz) {
        return columns.get(ColumnPos.hashPos(cx, cz));
    }

    public Column getColumn(long hashedPos) {
        return columns.get(hashedPos);
    }

    public boolean containsColumn(int cx, int cz) {
        return columns.containsKey(ColumnPos.hashPos(cx, cz));
    }

    public boolean containsColumn(long hashedPos) {
        return columns.containsKey(hashedPos);
    }

    public Iterable<Long2ObjectMap.Entry<Column>> iterateColumnMap() {
        return columns.long2ObjectEntrySet();
    }

    public ObjectIterator<Long2ObjectMap.Entry<Column>> getColumnMapIterator() {
        return columns.long2ObjectEntrySet().iterator();
    }

    public ObjectIterator<Long2ObjectMap.Entry<Column>> getColumnMapFastIterator() {
        return columns.long2ObjectEntrySet().fastIterator();
    }

    public Iterable<Column> iterateColumns() {
        return columns.values();
    }

    public void addColumn(int cx, int cz, Column column) {
        columns.put(ColumnPos.hashPos(cx, cz), column);
    }

    public void addColumn(long hashedPos, Column column) {
        columns.put(hashedPos, column);
    }

    public void removeColumn(int cx, int cz) {
        columns.remove(ColumnPos.hashPos(cx, cz));
    }

    public void removeColumn(long hashedPos) {
        columns.remove(hashedPos);
    }

    @Override
    public int getBlockFullID(int x, int y, int z) {
        int cx = ColumnPos.toColumnX(x);
        int cz = ColumnPos.toColumnZ(z);

        Column column = columns.get(ColumnPos.hashPos(cx, cz));
        if (column == null)
            throw new RuntimeException("Cannot query a block in an unloaded column.");

        int localX = ColumnPos.toLocalX(cx, x);
        int localZ = ColumnPos.toLocalZ(cz, z);

        return column.getBlockFullID(localX, y, localZ);
    }

    @Override
    public void setBlockFullID(int x, int y, int z, int to) {
        Column column = getColumnFromGlobalPos(x, z, "Cannot place/replace a block in an unloaded column.");
        int localX = ColumnPos.toLocalX(ColumnPos.toColumnX(x), x);
        int localZ = ColumnPos.toLocalZ(ColumnPos.toColumnZ(z), z);

        column.setBlockFullID(localX, y, localZ, to);
    }

    @Override
    public int getTopLevel(int x, int z) {
        Column column = getColumnFromGlobalPos(x, z, "Cannot query a block in an unloaded column.");
        int localX = ColumnPos.toLocalX(ColumnPos.toColumnX(x), x);
        int localZ = ColumnPos.toLocalZ(ColumnPos.toColumnZ(z), z);

        return column.getTopLevel(localX, localZ);
    }

    @Override
    public boolean placeBlockIfEmpty(int x, int y, int z, short to) {
        Column column = getColumnFromGlobalPos(x, z, "Cannot place a block in an unloaded column.");
        int localX = ColumnPos.toLocalX(ColumnPos.toColumnX(x), x);
        int localZ = ColumnPos.toLocalZ(ColumnPos.toColumnZ(z), z);

        return column.placeBlockIfEmpty(localX, y, localZ, to);
    }

    @Override
    public boolean replaceBlockIfExists(int x, int y, int z, short replacement) {
        Column column = getColumnFromGlobalPos(x, z, "Cannot replace a block in an unloaded column.");
        int localX = ColumnPos.toLocalX(ColumnPos.toColumnX(x), x);
        int localZ = ColumnPos.toLocalZ(ColumnPos.toColumnZ(z), z);

        return column.replaceBlockIfExists(localX, y, localZ, replacement);
    }

    private Column getColumnFromGlobalPos(int x, int z, String exceptionMessage) {
        int cx = ColumnPos.toColumnX(x);
        int cz = ColumnPos.toColumnZ(z);

        Column column = columns.get(ColumnPos.hashPos(cx, cz));
        if (column == null)
            throw new RuntimeException(exceptionMessage);
        
        return column;
    }
}