package net.survival.block

interface ColumnProvider {
    fun provideColumn(hashedPos: Long): Column

    fun provideColumn(cx: Int, cz: Int): Column {
        return provideColumn(ColumnPos.hashPos(cx, cz))
    }
}