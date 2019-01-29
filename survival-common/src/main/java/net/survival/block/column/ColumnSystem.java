package net.survival.block.column;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import net.survival.block.BlockSpace;

public class ColumnSystem
{
    private static final int GENERATOR_LOAD_RATE = 2;
    private static final int DATABASE_LOAD_RATE = 4;
    private static final double SAVE_RATE = 10.0;

    private final BlockSpace blockSpace;
    private final ColumnStageMask columnStageMask;

    private final ColumnDbPipe.ClientSide columnDbPipe;
    private final ColumnProvider columnGenerator;

    private final HashSet<Long> loadingColumns = new HashSet<>();
    private double saveTimer = SAVE_RATE;

    public ColumnSystem(
            BlockSpace blockSpace,
            ColumnStageMask columnStageMask,
            ColumnDbPipe.ClientSide columnDbPipe,
            ColumnProvider columnGenerator)
    {
        this.blockSpace = blockSpace;
        this.columnStageMask = columnStageMask;
        this.columnDbPipe = columnDbPipe;
        this.columnGenerator = columnGenerator;
    }

    public void saveAllColumns() {
        saveColumns();
    }

    public void update(double elapsedTime) {
        saveTimer -= elapsedTime;
        if (saveTimer <= 0.0) {
            saveColumns();
            maskOutColumns();
            saveTimer = SAVE_RATE;
        }

        var missingColumns = columnStageMask.getColumnPositions().stream()
                .filter(e -> !blockSpace.containsColumn(e))
                .collect(Collectors.toSet());

        loadMissingColumnsFromDb(missingColumns);
        generateColumns(missingColumns.iterator());
    }

    private void saveColumns() {
        // Save all loaded columns.
        for (var entry : blockSpace.iterateColumnMap()) {
            var hashedPos = entry.getKey();
            var column = entry.getValue();

            columnDbPipe.request(ColumnRequest.createPostRequest(hashedPos, column));
        }
    }

    private void maskOutColumns() {
        var mask = columnStageMask.getColumnPositions();
        var newColumnMap = blockSpace.getColumnMap().entrySet().stream()
                .filter(e -> mask.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        blockSpace.setColumnMap(newColumnMap);
    }

    private void loadMissingColumnsFromDb(Set<Long> missingColumns) {
        var iterator = missingColumns.iterator();

        for (var i = 0; iterator.hasNext() && i < DATABASE_LOAD_RATE; ++i) {
            var hashedPos = iterator.next();

            if (!loadingColumns.contains(hashedPos)) {
                loadingColumns.add(hashedPos);
                columnDbPipe.request(ColumnRequest.createGetRequest(hashedPos));
            }
        }

        missingColumns.clear();

        for (
                var response = columnDbPipe.pollResponse();
                response != null;
                response = columnDbPipe.pollResponse())
        {
            var hashedPos = response.columnPos;
            var column = response.column;

            loadingColumns.remove(hashedPos);

            if (column != null)
                blockSpace.addColumn(hashedPos, column);
            else
                missingColumns.add(hashedPos);
        }
    }

    private void generateColumns(Iterator<Long> missingColumns) {
        for (var i = 0; i < GENERATOR_LOAD_RATE && missingColumns.hasNext(); ++i) {
            var hashedPos = missingColumns.next();
            var generatedColumn = columnGenerator.provideColumn(hashedPos);
            blockSpace.addColumn(hashedPos, generatedColumn);
            missingColumns.remove();
        }
    }
}