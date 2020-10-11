package net.survival.gen

import net.survival.block.Column
import net.survival.block.ColumnProvider

class DefaultColumnGenerator(seed: Long): ColumnProvider {
    private val terrainGenerator: DefaultTerrainGenerator = DefaultTerrainGenerator(seed)
    private val decorator: DefaultColumnDecorator = DefaultColumnDecorator(true)
    private val primer: ColumnPrimer = ColumnPrimer()

    override fun provideColumn(hashedPos: Long): Column {
        primer.clear()
        terrainGenerator.generate(hashedPos, primer)
        decorator.decorate(hashedPos, primer)
        return primer.toColumn()
    }
}