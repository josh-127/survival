package net.survival.client.input

object Mouse {
    const val MODE_CENTERED = 0
    const val MODE_NORMAL = 1

    var x = 0.0
    var y = 0.0
    var prevX = 0.0
    var prevY = 0.0
    var isLmbDown = false
    var isRmbDown = false
    var prevLmbDown = false
    var prevRmbDown = false
    var mode = 0; private set
    var modeChanged = true

    val deltaX: Double get() = x - prevX
    val deltaY: Double get() = y - prevY
    val isLmbUp: Boolean get() = !isLmbDown
    val isLmbPressed: Boolean get() = isLmbDown && !prevLmbDown
    val isLmbReleased: Boolean get() = !isLmbDown && prevLmbDown
    val isRmbUp: Boolean get() = !isRmbDown
    val isRmbPressed: Boolean get() = isRmbDown && !prevRmbDown
    val isRmbReleased: Boolean get() = !isRmbDown && prevRmbDown

    fun changeMode(value: Int) {
        mode = value
        modeChanged = true
    }
}