package net.survival.client.input

abstract class MouseAdapter {
    protected fun setMousePosition(x: Double, y: Double) {
        Mouse.prevX = Mouse.x
        Mouse.prevY = Mouse.y
        Mouse.x = x
        Mouse.y = y
    }

    protected fun setLmbDown(down: Boolean) {
        Mouse.prevLmbDown = Mouse.isLmbDown
        Mouse.isLmbDown = down
    }

    protected fun setRmbDown(down: Boolean) {
        Mouse.prevRmbDown = Mouse.isRmbDown
        Mouse.isRmbDown = down
    }

    protected fun clearModeChangedFlag() {
        Mouse.modeChanged = false
    }
}