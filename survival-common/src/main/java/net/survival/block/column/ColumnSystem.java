package net.survival.block.column;

import java.util.HashSet;
import java.util.Map;
import java.util.stream.Collectors;

import net.survival.block.BlockSpace;

public class ColumnSystem
{
    private static final double SAVE_RATE = 10.0;

    private final BlockSpace blockSpace;
    private final ColumnStageMask columnStageMask;
    private final ColumnDbPipe.ClientSide columnPipe;

    private final HashSet<Long> loadingColumns = new HashSet<>();
    private double saveTimer = SAVE_RATE;

    public ColumnSystem(BlockSpace blockSpace, ColumnStageMask columnStageMask, ColumnDbPipe.ClientSide columnPipe) {
        this.blockSpace = blockSpace;
        this.columnStageMask = columnStageMask;
        this.columnPipe = columnPipe;
    }

    public void saveAllColumns() {
        for (var entry : blockSpace.iterateColumnMap()) {
            var hashedPos = entry.getKey();
            var column = entry.getValue();
            columnPipe.request(ColumnRequest.createPostRequest(hashedPos, column));
        }
    }

    public void update(double elapsedTime) {
        saveTimer -= elapsedTime;
        if (saveTimer <= 0.0) {
            saveTimer = SAVE_RATE;
            saveAllColumns();

            var mask = columnStageMask.getColumnPositions();
            var newColumnMap = blockSpace.getColumnMap().entrySet().stream()
                    .filter(e -> mask.contains(e.getKey()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            blockSpace.setColumnMap(newColumnMap);
        }

        var missingColumns = columnStageMask.getColumnPositions().stream()
                .filter(e -> !blockSpace.containsColumn(e))
                .filter(e -> !loadingColumns.contains(e))
                .collect(Collectors.toSet());

        for (var hashedPos : missingColumns) {
            loadingColumns.add(hashedPos);
            columnPipe.request(ColumnRequest.createGetRequest(hashedPos));
        }

        for (var response = columnPipe.pollResponse(); response != null; response = columnPipe.pollResponse()) {
            loadingColumns.remove(response.columnPos);
            blockSpace.addColumn(response.columnPos, response.column);
        }
    }
}