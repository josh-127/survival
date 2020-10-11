package net.survival.world

import net.survival.block.Column
import net.survival.block.io.ColumnDbPipe
import net.survival.block.io.ColumnRequest

class ColumnIO(
    private val columnPipe: ColumnDbPipe
) {
    var columns: MutableMap<Long, Column> = HashMap(); private set
    private val loadingColumns: MutableSet<Long> = HashSet()

    fun update() {
        var response = columnPipe.pollResponse()
        while (response != null) {
            val columnPos = response.columnPos
            val column = response.column
            loadingColumns.remove(columnPos)
            columns[columnPos] = column
            response = columnPipe.pollResponse()
        }
    }

    fun maskColumns(mask: Set<Long>) {
        // Check in columns.
        for ((columnPos, column) in columns) {
            columnPipe.request(ColumnRequest.Post(columnPos, column))
        }

        // Mask out columns.
        columns = HashMap(
            columns.entries
                .filter { mask.contains(it.key) }
                .associateBy({ it.key }, { it.value })
        )

        // Check out columns.
        mask.stream()
            .filter { !columns.containsKey(it) }
            .filter { !loadingColumns.contains(it) }
            .forEach {
                loadingColumns.add(it)
                columnPipe.request(ColumnRequest.Get(it))
            }
    }
}