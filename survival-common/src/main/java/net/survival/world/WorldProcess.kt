package net.survival.world

import net.survival.player.Player
import java.util.*

class WorldProcess {
    private val world: World = TransientWorld()

    fun tick(inputs: List<WorldProcessCommand>) {
    }
}

sealed class WorldProcessCommand {
    data class ControlPlayer(
        val playerUuid: UUID,
        val dx: Double,
        val dz: Double,
        val jump: Boolean,
    ): WorldProcessCommand()
    data class RegisterPlayer(
        val player: Player
    ): WorldProcessCommand()
}
