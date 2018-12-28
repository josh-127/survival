package net.survival.world.column;

import java.util.Iterator;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.LongArraySet;
import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.survival.world.World;
import net.survival.world.gen.decoration.WorldDecorator;

public class ColumnSystem
{
    private static final int GENERATOR_LOAD_RATE = 2;
    private static final int DATABASE_LOAD_RATE = 4;
    private static final double SAVE_RATE = 10.0;

    private final World world;
    private final ColumnStageMask columnStageMask;

    private final ColumnDbPipe.ClientSide columnDbPipe;
    private final ColumnProvider columnGenerator;
    private final WorldDecorator worldDecorator;

    private final LongArraySet loadingColumns;
    private double saveTimer;

    public ColumnSystem(
            World world,
            ColumnStageMask columnStageMask,
            ColumnDbPipe.ClientSide columnDbPipe,
            ColumnProvider columnGenerator,
            WorldDecorator worldDecorator)
    {
        this.world = world;
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

        LongSet missingColumns = getMissingColumnPosSet();
        loadMissingColumnsFromDb(missingColumns);
        generateColumns(missingColumns.iterator());
    }

    private LongSet getMissingColumnPosSet() {
        LongSet missingColumns = columnStageMask.getColumnPositions();

        ObjectIterator<Long2ObjectMap.Entry<Column>> iterator = world.getColumnMapFastIterator();
        while (iterator.hasNext()) {
            Long2ObjectMap.Entry<Column> entry = iterator.next();
            long hashedPos = entry.getLongKey();

            if (missingColumns.contains(hashedPos))
                missingColumns.remove(hashedPos);
        }

        return missingColumns;
    }

    private void saveColumns() {
        // Save all loaded columns.
        ObjectIterator<Long2ObjectMap.Entry<Column>> iterator = world.getColumnMapFastIterator();
        while (iterator.hasNext()) {
            Long2ObjectMap.Entry<Column> entry = iterator.next();
            long hashedPos = entry.getLongKey();
            Column column = entry.getValue();

            columnDbPipe.request(ColumnRequest.createPostRequest(hashedPos, column));
        }
    }

    private void maskOutColumns() {
        ObjectIterator<Long2ObjectMap.Entry<Column>> iterator = world.getColumnMapFastIterator();
        LongSet mask = columnStageMask.getColumnPositions();

        while (iterator.hasNext()) {
            Long2ObjectMap.Entry<Column> entry = iterator.next();
            long hashedPos = entry.getLongKey();

            if (!mask.contains(hashedPos))
                iterator.remove();
        }
    }

    private void loadMissingColumnsFromDb(LongSet missingColumns) {
        LongIterator iterator = missingColumns.iterator();

        for (int i = 0; iterator.hasNext() && i < DATABASE_LOAD_RATE; ++i) {
            long hashedPos = iterator.nextLong();

            if (!loadingColumns.contains(hashedPos)) {
                loadingColumns.add(hashedPos);
                columnDbPipe.request(ColumnRequest.createGetRequest(hashedPos));
            }
        }

        missingColumns.clear();

        for (
                ColumnResponse response = columnDbPipe.pollResponse();
                response != null;
                response = columnDbPipe.pollResponse())
        {
            long hashedPos = response.columnPos;
            Column column = response.column;

            loadingColumns.remove(hashedPos);

            if (column != null)
                world.addColumn(hashedPos, column);
            else
                missingColumns.add(hashedPos);
        }
    }

    private void generateColumns(LongIterator missingColumns) {
        for (int i = 0; i < GENERATOR_LOAD_RATE && missingColumns.hasNext(); ++i) {
            long hashedPos = missingColumns.nextLong();
            Column generatedColumn = columnGenerator.provideColumn(hashedPos);
            world.addColumn(hashedPos, generatedColumn);
            missingColumns.remove();
        }

        Iterator<Long2ObjectMap.Entry<Column>> columnMapIt = world.getColumnMapFastIterator();
        while (columnMapIt.hasNext()) {
            Long2ObjectMap.Entry<Column> entry = columnMapIt.next();
            long hashedPos = entry.getLongKey();
            int cx = ColumnPos.columnXFromHashedPos(hashedPos);
            int cz = ColumnPos.columnZFromHashedPos(hashedPos);
            Column column = entry.getValue();

            boolean isFullySurrounded = true;
            for (int z = -1; z <= 1 && isFullySurrounded; ++z) {
                for (int x = -1; x <= 1 && isFullySurrounded; ++x) {
                    if (world.getColumn(cx + x, cz + z) == null)
                        isFullySurrounded = false;
                }
            }

            if (!column.isDecorated() && isFullySurrounded) {
                worldDecorator.decorate(cx, cz, column, world);
                column.markDecorated();
            }
        }
    }
}