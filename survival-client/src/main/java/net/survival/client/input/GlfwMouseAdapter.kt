package net.survival.client.input

import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFWCursorPosCallbackI

class GlfwMouseAdapter(
    private val window: Long
): MouseAdapter(), GLFWCursorPosCallbackI {
    private var mouseX = 0.0
    private var mouseY = 0.0

    init {
        GLFW.glfwSetInputMode(window, GLFW.GLFW_STICKY_MOUSE_BUTTONS, GLFW.GLFW_TRUE)
    }

    fun tick() {
        setMousePosition(mouseX, mouseY)
        setLmbDown(GLFW.glfwGetMouseButton(window, GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS)
        setRmbDown(GLFW.glfwGetMouseButton(window, GLFW.GLFW_MOUSE_BUTTON_RIGHT) == GLFW.GLFW_PRESS)

        if (Mouse.modeChanged) {
            if (Mouse.mode == Mouse.MODE_NORMAL) {
                GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL)
            }
            else if (Mouse.mode == Mouse.MODE_CENTERED) {
                GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED)
            }
            clearModeChangedFlag()
        }
    }

    override fun invoke(window: Long, x: Double, y: Double) {
        mouseX = x
        mouseY = y
    }
}