package net.survival.player

import java.util.*

class Player(
    val uuid: UUID,
    val username: String,
    var displayName: String,
    var actorId: Int?
) {
    fun update(player: Player) {
        displayName = player.displayName
        actorId = player.actorId
    }
}
