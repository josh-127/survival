package net.survival.client.input

abstract class KeyboardAdapter {
    protected fun pressKey(key: Key) {
        Keyboard.keyState[key.ordinal] = true
    }

    protected fun releaseKey(key: Key) {
        Keyboard.keyState[key.ordinal] = false
    }

    fun nextInputFrame() {
        System.arraycopy(Keyboard.keyState, 0, Keyboard.prevKeyState, 0, Keyboard.prevKeyState.size)
    }
}