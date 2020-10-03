package net.survival.world;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import net.survival.block.Column;
import net.survival.block.io.ColumnDbPipe;
import net.survival.block.message.GetColumnRequest;
import net.survival.block.message.PostColumnRequest;

public class ColumnIO {
    private Map<Long, Column> columns = new HashMap<>();
    private Set<Long> loadingColumns = new HashSet<>();
    private final ColumnDbPipe.ClientSide columnPipe;

    public ColumnIO(ColumnDbPipe.ClientSide columnPipe) {
        this.columnPipe = columnPipe;
    }

    public Map<Long, Column> getColumns() {
        return columns;
    }

    public void update() {
        var response = columnPipe.pollResponse();

        while (response != null) {
            var columnPos = response.getColumnPos();
            var column = response.getColumn();
            loadingColumns.remove(columnPos);
            columns.put(columnPos, column);

            response = columnPipe.pollResponse();
        }
    }

    public void maskColumns(Set<Long> mask) {
        // Check in columns.
        for (var entry : columns.entrySet()) {
            var columnPos = entry.getKey();
            var column = entry.getValue();
            columnPipe.request(new PostColumnRequest(columnPos, column));
        }

        // Mask out columns.
        columns = columns.entrySet().stream()
                .filter(e -> mask.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        // Check out columns.
        mask.stream()
                .filter(pos -> !columns.containsKey(pos))
                .filter(pos -> !loadingColumns.contains(pos))
                .forEach(pos -> {
                    loadingColumns.add(pos);
                    columnPipe.request(new GetColumnRequest(pos));
                });
    }
}