package net.survival.world

import net.survival.actor.Actor
import net.survival.block.Block
import net.survival.block.Column
import net.survival.block.ColumnPos
import net.survival.player.Player
import java.util.*

class TransientWorld: World {
    private val actors: MutableMap<Long, Actor> = LinkedHashMap()
    private val columns: MutableMap<Long, Column> = LinkedHashMap()
    private val players: MutableMap<UUID, Player> = LinkedHashMap()
    private var changes: ArrayList<WorldCommand> = ArrayList()

    fun getAndResetChanges(): List<WorldCommand> {
        val result = changes
        changes = ArrayList()
        return result
    }

    //
    // Actors
    //
    override fun getActors(): Iterable<Actor> = actors.values
    override fun getActorOrNull(id: Long): Actor? = actors[id]

    override fun insertActor(actor: Actor) {
        if (actors.containsKey(actor.id)) {
            throw Exception("Actor ${actor.id} already exists.")
        }
        actors[actor.id] = actor
        changes.add(WorldCommand.InsertActor(actor))
    }

    override fun deleteActor(id: Long) {
        if (actors.remove(id) == null) {
            throw Exception("Actor $id does not exist.")
        }
        changes.add(WorldCommand.DeleteActor(id))
    }

    override fun updateActor(actor: Actor) {
        getActor(actor.id).update(actor)
        changes.add(WorldCommand.UpdateActor(actor))
    }

    //
    // Columns
    //
    override fun getColumnOrNull(columnPos: Long): Column? = columns[columnPos]

    override fun setColumn(columnPos: Long, column: Column?) {
        if (column != null) {
            val copy = Column(column)
            columns[columnPos] = copy
            changes.add(WorldCommand.SetColumn(columnPos, copy))
        }
        else {
            columns.remove(columnPos)
            changes.add(WorldCommand.SetColumn(columnPos, null))
        }
    }

    override fun getBlockOrNull(x: Int, y: Int, z: Int): Block? {
        val cx = ColumnPos.toColumnX(x)
        val cz = ColumnPos.toColumnZ(z)
        val column = columns[ColumnPos.hashPos(cx, cz)] ?: return null
        val lx = ColumnPos.toLocalX(cx, x)
        val lz = ColumnPos.toLocalZ(cz, z)
        return column.getBlock(lx, y, lz)
    }

    override fun setBlock(x: Int, y: Int, z: Int, block: Block) {
        val cx = ColumnPos.toColumnX(x)
        val cz = ColumnPos.toColumnZ(z)
        val column = columns[ColumnPos.hashPos(cx, cz)]
            ?: throw Exception("Cannot set block at ($x, $y, $z) to $block from an unloaded column.")
        val lx = ColumnPos.toLocalX(ColumnPos.toColumnX(x), x)
        val lz = ColumnPos.toLocalZ(ColumnPos.toColumnZ(z), z)
        column.setBlock(lx, y, lz, block)
        changes.add(WorldCommand.SetBlock(x, y, z, block))
    }

    //
    // Players
    //
    override fun getPlayers(): Iterable<Player> = players.values
    override fun getPlayerOrNull(uuid: UUID): Player? = players[uuid]

    override fun insertPlayer(player: Player) {
        if (players[player.uuid] != null) {
            throw Exception("Player ${player.uuid} already exists.")
        }
        players[player.uuid] = player
        changes.add(WorldCommand.InsertPlayer(player))
    }

    override fun deletePlayer(uuid: UUID) {
        if (players.remove(uuid) == null) {
            throw Exception("Player $uuid does not exist.")
        }
        changes.add(WorldCommand.DeletePlayer(uuid))
    }

    override fun updatePlayer(player: Player) {
        getPlayer(player.uuid).update(player)
        changes.add(WorldCommand.UpdatePlayer(player))
    }
}
