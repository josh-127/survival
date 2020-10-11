package net.survival.world

import net.survival.actor.Actor
import net.survival.block.Block
import net.survival.block.Column
import net.survival.block.ColumnPos
import java.util.*

class World {
    private val actors = ArrayList<Actor>()
    val columns: MutableMap<Long, Column> = HashMap()

    fun addActor(actor: Actor) {
        actors.add(actor)
    }

    fun getBlock(x: Int, y: Int, z: Int): Block {
        val cx = ColumnPos.toColumnX(x)
        val cz = ColumnPos.toColumnZ(z)
        val column = columns[ColumnPos.hashPos(cx, cz)]
            ?: throw RuntimeException("Cannot query a block in an unloaded column.")
        val lx = ColumnPos.toLocalX(cx, x)
        val lz = ColumnPos.toLocalZ(cz, z)
        return column.getBlock(lx, y, lz)
    }

    fun setBlock(x: Int, y: Int, z: Int, to: Block?) {
        val cx = ColumnPos.toColumnX(x)
        val cz = ColumnPos.toColumnZ(z)
        val column = columns[ColumnPos.hashPos(cx, cz)]
            ?: throw RuntimeException("Cannot place/replace a block in an unloaded column.")
        val lx = ColumnPos.toLocalX(ColumnPos.toColumnX(x), x)
        val lz = ColumnPos.toLocalZ(ColumnPos.toColumnZ(z), z)
        column.setBlock(lx, y, lz, to)
    }
}