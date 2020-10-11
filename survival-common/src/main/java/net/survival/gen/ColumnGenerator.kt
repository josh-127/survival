package net.survival.gen

import net.survival.block.Column

class ColumnGenerator(seed: Long) {
    private val terrainGenerator: DefaultTerrainGenerator = DefaultTerrainGenerator(seed)
    private val decorator: DefaultColumnDecorator = DefaultColumnDecorator(true)
    private val primer: ColumnPrimer = ColumnPrimer()

    fun generate(columnPos: Long): Column {
        primer.clear()
        terrainGenerator.generate(columnPos, primer)
        decorator.decorate(columnPos, primer)
        return primer.toColumn()
    }
}