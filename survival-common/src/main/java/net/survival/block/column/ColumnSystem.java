package net.survival.block.column;

import it.unimi.dsi.fastutil.longs.LongArraySet;
import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.LongOpenHashBigSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.survival.block.BlockSpace;
import net.survival.gen.decoration.WorldDecorator;

public class ColumnSystem
{
    private static final int GENERATOR_LOAD_RATE = 2;
    private static final int DATABASE_LOAD_RATE = 4;
    private static final double SAVE_RATE = 10.0;

    private final BlockSpace blockSpace;
    private final ColumnStageMask columnStageMask;

    private final ColumnDbPipe.ClientSide columnDbPipe;
    private final ColumnProvider columnGenerator;
    private final WorldDecorator worldDecorator;

    private final LongArraySet loadingColumns;
    private double saveTimer;

    public ColumnSystem(
            BlockSpace blockSpace,
            ColumnStageMask columnStageMask,
            ColumnDbPipe.ClientSide columnDbPipe,
            ColumnProvider columnGenerator,
            WorldDecorator worldDecorator)
    {
        this.blockSpace = blockSpace;
        this.columnStageMask = columnStageMask;
        this.columnDbPipe = columnDbPipe;
        this.columnGenerator = columnGenerator;
        this.worldDecorator = worldDecorator;

        loadingColumns = new LongArraySet();
        saveTimer = SAVE_RATE;
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

        var missingColumnsStream = columnStageMask.getColumnPositions().stream()
                .filter(e -> !blockSpace.containsColumn(e));

        var missingColumns = new LongOpenHashBigSet(missingColumnsStream.iterator());
        loadMissingColumnsFromDb(missingColumns);
        generateColumns(missingColumns.iterator());
    }

    private void saveColumns() {
        // Save all loaded columns.
        var iterator = blockSpace.getColumnMapFastIterator();
        while (iterator.hasNext()) {
            var entry = iterator.next();
            var hashedPos = entry.getLongKey();
            var column = entry.getValue();

            columnDbPipe.request(ColumnRequest.createPostRequest(hashedPos, column));
        }
    }

    private void maskOutColumns() {
        var iterator = blockSpace.getColumnMapFastIterator();
        var mask = columnStageMask.getColumnPositions();

        while (iterator.hasNext()) {
            var entry = iterator.next();
            var hashedPos = entry.getLongKey();

            if (!mask.contains(hashedPos))
                iterator.remove();
        }
    }

    private void loadMissingColumnsFromDb(LongSet missingColumns) {
        var iterator = missingColumns.iterator();

        for (var i = 0; iterator.hasNext() && i < DATABASE_LOAD_RATE; ++i) {
            var hashedPos = iterator.nextLong();

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

    private void generateColumns(LongIterator missingColumns) {
        for (var i = 0; i < GENERATOR_LOAD_RATE && missingColumns.hasNext(); ++i) {
            var hashedPos = missingColumns.nextLong();
            var generatedColumn = columnGenerator.provideColumn(hashedPos);
            blockSpace.addColumn(hashedPos, generatedColumn);
            missingColumns.remove();
        }

        var columnMapIt = blockSpace.getColumnMapFastIterator();
        while (columnMapIt.hasNext()) {
            var entry = columnMapIt.next();
            var hashedPos = entry.getLongKey();
            var cx = ColumnPos.columnXFromHashedPos(hashedPos);
            var cz = ColumnPos.columnZFromHashedPos(hashedPos);
            var column = entry.getValue();

            var isFullySurrounded = true;
            for (var z = -1; z <= 1 && isFullySurrounded; ++z) {
                for (var x = -1; x <= 1 && isFullySurrounded; ++x) {
                    if (blockSpace.getColumn(cx + x, cz + z) == null)
                        isFullySurrounded = false;
                }
            }

            if (!column.isDecorated() && isFullySurrounded) {
                worldDecorator.decorate(cx, cz, column, blockSpace);
                column.markDecorated();
            }
        }
    }
}