package net.survival.client.input

object Keyboard {
    var prevKeyState = BooleanArray(Key.cachedValues.size)
    var keyState = BooleanArray(Key.cachedValues.size)

    fun isKeyDown(key: Key): Boolean = keyState[key.ordinal]
    fun isKeyUp(key: Key): Boolean = !keyState[key.ordinal]

    fun isKeyPressed(key: Key): Boolean =
        keyState[key.ordinal] && !prevKeyState[key.ordinal]

    fun isKeyReleased(key: Key): Boolean =
        !keyState[key.ordinal] && prevKeyState[key.ordinal]
}