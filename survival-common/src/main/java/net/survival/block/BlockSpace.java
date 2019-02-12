package net.survival.block;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import net.survival.block.message.BlockMessageVisitor;
import net.survival.block.message.BreakBlockMessage;
import net.survival.block.message.ColumnResponseMessage;
import net.survival.block.message.MaskColumnsMessage;
import net.survival.block.message.PlaceBlockMessage;
import net.survival.interaction.InteractionContext;
import net.survival.render.message.InvalidateColumnMessage;

public class BlockSpace implements BlockMessageVisitor
{
    private Map<Long, Column> columns = new HashMap<>();
    private Set<Long> loadingColumns = new HashSet<>();

    private final ColumnDbPipe.ClientSide columnPipe;

    public BlockSpace(ColumnDbPipe.ClientSide columnPipe) {
        this.columnPipe = columnPipe;
    }

    public Map<Long, Column> getColumnMap() { return columns; }
    public void setColumnMap(Map<Long, Column> to) { columns = to; }

    public Column getColumn(int cx, int cz) { return columns.get(ColumnPos.hashPos(cx, cz)); }
    public Column getColumn(long hashedPos) { return columns.get(hashedPos); }

    public boolean containsColumn(int cx, int cz) { return columns.containsKey(ColumnPos.hashPos(cx, cz)); }
    public boolean containsColumn(long hashedPos) { return columns.containsKey(hashedPos); }

    public Iterable<Map.Entry<Long, Column>> iterateColumnMap() { return columns.entrySet(); }
    public Iterable<Column> iterateColumns() { return columns.values(); }

    public void addColumn(int cx, int cz, Column column) { columns.put(ColumnPos.hashPos(cx, cz), column); }
    public void addColumn(long hashedPos, Column column) { columns.put(hashedPos, column); }
    public void removeColumn(int cx, int cz) { columns.remove(ColumnPos.hashPos(cx, cz)); }
    public void removeColumn(long hashedPos) { columns.remove(hashedPos); }

    public int getBlockFullID(int x, int y, int z) {
        var cx = ColumnPos.toColumnX(x);
        var cz = ColumnPos.toColumnZ(z);

        var column = columns.get(ColumnPos.hashPos(cx, cz));
        if (column == null)
            throw new RuntimeException("Cannot query a block in an unloaded column.");

        var localX = ColumnPos.toLocalX(cx, x);
        var localZ = ColumnPos.toLocalZ(cz, z);

        return column.getBlockFullID(localX, y, localZ);
    }

    private void setBlockFullID(int x, int y, int z, int to) {
        var column = getColumnFromGlobalPos(x, z, "Cannot place/replace a block in an unloaded column.");
        var localX = ColumnPos.toLocalX(ColumnPos.toColumnX(x), x);
        var localZ = ColumnPos.toLocalZ(ColumnPos.toColumnZ(z), z);

        column.setBlockFullID(localX, y, localZ, to);
    }

    private Column getColumnFromGlobalPos(int x, int z, String exceptionMessage) {
        var cx = ColumnPos.toColumnX(x);
        var cz = ColumnPos.toColumnZ(z);

        var column = columns.get(ColumnPos.hashPos(cx, cz));
        if (column == null)
            throw new RuntimeException(exceptionMessage);
        
        return column;
    }

    private void invalidateColumn(InteractionContext ic, int x, int z) {
        var cx = ColumnPos.toColumnX(x);
        var cz = ColumnPos.toColumnZ(z);

        var column = columns.get(ColumnPos.hashPos(cx, cz));
        if (column == null)
            throw new RuntimeException("Cannot invalidate an unloaded column.");

        ic.postMessage(new InvalidateColumnMessage(ColumnPos.hashPos(cx, cz), column));
    }

    @Override
    public void visit(InteractionContext ic, BreakBlockMessage message) {
        setBlockFullID(message.getX(), message.getY(), message.getZ(), 0);
        invalidateColumn(ic, message.getX(), message.getZ());
        ic.burstParticles(message.getX() + 0.5, message.getY() + 0.5, message.getZ() + 0.5, 2.0, 8);
    }

    @Override
    public void visit(InteractionContext ic, PlaceBlockMessage message) {
        setBlockFullID(message.getX(), message.getY(), message.getZ(), message.getFullID());
        invalidateColumn(ic, message.getX(), message.getZ());
    }

    @Override
    public void visit(InteractionContext ic, ColumnResponseMessage message) {
        var response = message.getColumnResponse();
        loadingColumns.remove(response.columnPos);
        columns.put(response.columnPos, response.column);

        var cx = ColumnPos.columnXFromHashedPos(response.columnPos);
        var cz = ColumnPos.columnZFromHashedPos(response.columnPos);
        var x = ColumnPos.toGlobalX(cx, 0);
        var z = ColumnPos.toGlobalZ(cz, 0);
        invalidateColumn(ic, x, z);
    }

    @Override
    public void visit(InteractionContext ic, MaskColumnsMessage message) {
        // Check in columns.
        for (var entry : columns.entrySet()) {
            var columnPos = entry.getKey();
            var column = entry.getValue();
            columnPipe.request(ColumnRequest.createPostRequest(columnPos, column));
        }

        // Mask out columns.
        var columnPositions = message.getMask().getColumnPositions();

        columns = columns.entrySet().stream()
                .filter(e -> columnPositions.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        // Check out columns.
        var missingPositions = columnPositions.stream()
                .filter(e -> !columns.containsKey(e))
                .filter(e -> !loadingColumns.contains(e))
                .collect(Collectors.toSet());

        for (var columnPos : missingPositions) {
            loadingColumns.add(columnPos);
            columnPipe.request(ColumnRequest.createGetRequest(columnPos));
        }
    }
}