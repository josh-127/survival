package net.survival.world

import net.survival.actor.Actor
import net.survival.actor.putActor
import net.survival.block.Block
import net.survival.block.Column
import net.survival.block.io.ColumnCodec
import net.survival.player.Player
import java.nio.ByteBuffer
import java.util.*

interface World {
    //
    // Actors
    //
    fun getActors(): Iterable<Actor>
    fun getActor(id: Long): Actor {
        return getActorOrNull(id) ?: throw Exception("Actor $id does not exist.")
    }
    fun getActorOrNull(id: Long): Actor?
    fun containsActor(id: Long): Boolean {
        return getActorOrNull(id) != null
    }
    fun insertActor(actor: Actor)
    fun deleteActor(id: Long)
    fun updateActor(actor: Actor)
    fun markActor(id: Long) {
        updateActor(getActor(id))
    }

    //
    // Columns
    //
    fun getColumn(columnPos: Long): Column {
        return getColumnOrNull(columnPos) ?: throw Exception("Column at ${columnPos.toString(16)} does not exist.");
    }
    fun getColumnOrNull(columnPos: Long): Column?
    fun containsColumn(columnPos: Long): Boolean {
        return getColumnOrNull(columnPos) != null
    }
    fun setColumn(columnPos: Long, column: Column?)

    fun getBlock(x: Int, y: Int, z: Int): Block {
        return getBlockOrNull(x, y, z) ?: throw Exception("Cannot get block ($x, $y, $z) from a nonexistent column.")
    }
    fun getBlockOrNull(x: Int, y: Int, z: Int): Block?
    fun setBlock(x: Int, y: Int, z: Int, block: Block)

    //
    // Players
    //
    fun getPlayers(): Iterable<Player>
    fun getPlayer(uuid: UUID): Player {
        return getPlayerOrNull(uuid) ?: throw Exception("Player $uuid does not exist.")
    }
    fun getPlayerOrNull(uuid: UUID): Player?
    fun containsPlayer(uuid: UUID): Boolean {
        return getPlayerOrNull(uuid) != null
    }
    fun insertPlayer(player: Player)
    fun deletePlayer(uuid: UUID)
    fun updatePlayer(player: Player)
    fun markPlayer(uuid: UUID) {
        updatePlayer(getPlayer(uuid))
    }

    fun replicate(changes: List<WorldCommand>) {
        for (ch in changes) {
            when (ch) {
                is WorldCommand.InsertActor -> insertActor(ch.actor)
                is WorldCommand.DeleteActor -> deleteActor(ch.id)
                is WorldCommand.UpdateActor -> updateActor(ch.actor)
                is WorldCommand.SetColumn -> setColumn(ch.columnPos, ch.column)
                is WorldCommand.SetBlock -> setBlock(ch.x, ch.y, ch.z, ch.block)
                is WorldCommand.InsertPlayer -> insertPlayer(ch.player)
                is WorldCommand.DeletePlayer -> deletePlayer(ch.uuid)
                is WorldCommand.UpdatePlayer -> updatePlayer(ch.player)
            }
        }
    }
}

sealed class WorldCommand {
    data class InsertActor(val actor: Actor): WorldCommand()
    data class DeleteActor(val id: Long): WorldCommand()
    data class UpdateActor(val actor: Actor): WorldCommand()
    data class SetColumn(val columnPos: Long, val column: Column?): WorldCommand()
    data class SetBlock(val x: Int, val y: Int, val z: Int, val block: Block): WorldCommand()
    data class InsertPlayer(val player: Player): WorldCommand()
    data class DeletePlayer(val uuid: UUID): WorldCommand()
    data class UpdatePlayer(val player: Player): WorldCommand()

    companion object {
        // TODO("Needs a proper serialization framework")
        fun serialize(commandList: List<WorldCommand>, dest: ByteBuffer) {
            for (cmd in commandList) {
                when (cmd) {
                    is InsertActor -> { dest.put(0); dest.putActor(cmd.actor) }
                    is DeleteActor -> { dest.put(1); dest.putLong(cmd.id) }
                    is UpdateActor -> { dest.put(2); dest.putActor(cmd.actor) }
                    is SetColumn -> {
                        dest.put(3)
                        dest.putLong(cmd.columnPos)
                        if (cmd.column != null) { dest.put(1); ColumnCodec.compressColumn(cmd.column, dest) }
                        else { dest.put(0) }
                    }
                    is SetBlock -> {
                        dest.put(4)
                        dest.putInt(cmd.x); dest.putInt(cmd.y); dest.putInt(cmd.z)
                        ColumnCodec.serializeBlock(cmd.block, dest)
                    }
                    else -> throw Exception("Not implemented yet.")
                }
            }
        }
    }
}
