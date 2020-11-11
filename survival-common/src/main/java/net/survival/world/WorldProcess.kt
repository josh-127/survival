package net.survival.world

import net.survival.actor.Physics
import net.survival.player.Player
import java.util.*

class WorldProcess(private val world: TransientWorld) {
    fun tick(inputs: List<WorldProcessCommand>, elapsedTime: Double) {
        // Process Inputs
        for (it in inputs) {
            when (it) {
                is WorldProcessCommand.ControlPlayer -> {
                    val player = world.getPlayer(it.playerUuid)
                    val actorId = player.actorId ?: throw Exception("Player is not assigned to an actor.")
                    val actor = world.getActor(actorId)
                    actor.move(it.deltaX, it.deltaZ)
                    if (it.jump) {
                        actor.jump(1.1)
                    }
                }
                else -> {}
            }
        }

        // Update
        for (actor in world.getActors()) {
            Physics.tick(actor, world, elapsedTime)
            world.updateActor(actor)
        }
    }
}

sealed class WorldProcessCommand {
    data class ControlPlayer(
        val playerUuid: UUID,
        val deltaX: Double,
        val deltaZ: Double,
        val jump: Boolean,
    ): WorldProcessCommand()
    data class RegisterPlayer(
        val player: Player
    ): WorldProcessCommand()
}
