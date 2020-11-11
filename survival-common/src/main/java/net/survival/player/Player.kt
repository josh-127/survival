package net.survival.player

import java.util.*

class Player(
    val uuid: UUID,
    val username: String,
    var displayName: String,
    var actorId: Long?
) {
    fun update(player: Player) {
        displayName = player.displayName
        actorId = player.actorId
    }
}
