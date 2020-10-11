package net.survival.block.io

import net.survival.block.Column

sealed class ColumnRequest {
    object Close: ColumnRequest()
    data class Get(val columnPos: Long): ColumnRequest()
    data class Post(val columnPos: Long, val column: Column): ColumnRequest()
}

data class ColumnResponse(val columnPos: Long, val column: Column)
