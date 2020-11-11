package net.survival.world

import net.survival.actor.Actor
import net.survival.block.Block
import net.survival.block.Column
import net.survival.player.Player
import java.io.File
import java.util.*

class PersistentWorld(val file: File): World {
    override fun getActors(): Iterable<Actor> {
        TODO("Not yet implemented")
    }

    override fun getActorOrNull(id: Long): Actor? {
        TODO("Not yet implemented")
    }

    override fun insertActor(actor: Actor) {
        TODO("Not yet implemented")
    }

    override fun deleteActor(id: Long) {
        TODO("Not yet implemented")
    }

    override fun updateActor(actor: Actor) {
        TODO("Not yet implemented")
    }

    override fun getColumnOrNull(columnPos: Long): Column? {
        TODO("Not yet implemented")
    }

    override fun setColumn(columnPos: Long, column: Column?) {
        TODO("Not yet implemented")
    }

    override fun getBlockOrNull(x: Int, y: Int, z: Int): Block? {
        TODO("Not yet implemented")
    }

    override fun setBlock(x: Int, y: Int, z: Int, block: Block) {
        TODO("Not yet implemented")
    }

    override fun getPlayers(): Iterable<Player> {
        TODO("Not yet implemented")
    }

    override fun getPlayerOrNull(uuid: UUID): Player? {
        TODO("Not yet implemented")
    }

    override fun insertPlayer(player: Player) {
        TODO("Not yet implemented")
    }

    override fun deletePlayer(uuid: UUID) {
        TODO("Not yet implemented")
    }

    override fun updatePlayer(player: Player) {
        TODO("Not yet implemented")
    }
}
