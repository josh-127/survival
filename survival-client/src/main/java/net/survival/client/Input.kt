package net.survival.client

import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFWCursorPosCallbackI
import org.lwjgl.glfw.GLFWKeyCallbackI

interface Keyboard {
    fun isKeyDown(key: Key): Boolean
    fun isKeyUp(key: Key): Boolean
    fun isKeyPressed(key: Key): Boolean
    fun isKeyReleased(key: Key): Boolean
}

class MutableKeyboard: Keyboard {
    private val writeBuffer: KeyBuffer = KeyBuffer()
    private val readBuffers: Array<KeyBuffer> = Array(2) { KeyBuffer() }
    @Volatile
    private var swapIndex: Int = 0

    private val currentBuffer get() = readBuffers[swapIndex and 1]
    private val nextBuffer get() = readBuffers[(swapIndex + 1) and 1]
    private val keyState get() = currentBuffer.keyState
    private val prevKeyState get() = currentBuffer.prevKeyState

    override fun isKeyDown(key: Key): Boolean = keyState[key.ordinal]
    override fun isKeyUp(key: Key): Boolean = !keyState[key.ordinal]
    override fun isKeyPressed(key: Key): Boolean = keyState[key.ordinal] && !prevKeyState[key.ordinal]
    override fun isKeyReleased(key: Key): Boolean = !keyState[key.ordinal] && prevKeyState[key.ordinal]

    fun setKeyState(key: Key, pressed: Boolean) {
        writeBuffer.prevKeyState[key.ordinal] = writeBuffer.keyState[key.ordinal]
        writeBuffer.keyState[key.ordinal] = pressed
    }

    fun swapBuffers() {
        writeBuffer.keyState.copyInto(nextBuffer.keyState)
        writeBuffer.prevKeyState.copyInto(nextBuffer.prevKeyState)
        swapIndex++
    }

    private class KeyBuffer {
        val keyState: BooleanArray = BooleanArray(Key.cachedValues.size)
        val prevKeyState: BooleanArray = BooleanArray(Key.cachedValues.size)
    }
}

class GlfwKeyboardAdapter(private val keyboard: MutableKeyboard): GLFWKeyCallbackI {
    override fun invoke(window: Long, key: Int, scancode: Int, action: Int, mods: Int) {
        val convertedKey = when (key) {
            GLFW.GLFW_KEY_SPACE -> Key.SPACE
            GLFW.GLFW_KEY_APOSTROPHE -> Key.APOSTROPHE
            GLFW.GLFW_KEY_COMMA -> Key.COMMA
            GLFW.GLFW_KEY_MINUS -> Key.MINUS
            GLFW.GLFW_KEY_PERIOD -> Key.PERIOD
            GLFW.GLFW_KEY_SLASH -> Key.SLASH
            GLFW.GLFW_KEY_0 -> Key._0
            GLFW.GLFW_KEY_1 -> Key._1
            GLFW.GLFW_KEY_2 -> Key._2
            GLFW.GLFW_KEY_3 -> Key._3
            GLFW.GLFW_KEY_4 -> Key._4
            GLFW.GLFW_KEY_5 -> Key._5
            GLFW.GLFW_KEY_6 -> Key._6
            GLFW.GLFW_KEY_7 -> Key._7
            GLFW.GLFW_KEY_8 -> Key._8
            GLFW.GLFW_KEY_9 -> Key._9
            GLFW.GLFW_KEY_SEMICOLON -> Key.SEMICOLON
            GLFW.GLFW_KEY_EQUAL -> Key.EQUAL
            GLFW.GLFW_KEY_A -> Key.A
            GLFW.GLFW_KEY_B -> Key.B
            GLFW.GLFW_KEY_C -> Key.C
            GLFW.GLFW_KEY_D -> Key.D
            GLFW.GLFW_KEY_E -> Key.E
            GLFW.GLFW_KEY_F -> Key.F
            GLFW.GLFW_KEY_G -> Key.G
            GLFW.GLFW_KEY_H -> Key.H
            GLFW.GLFW_KEY_I -> Key.I
            GLFW.GLFW_KEY_J -> Key.J
            GLFW.GLFW_KEY_K -> Key.K
            GLFW.GLFW_KEY_L -> Key.L
            GLFW.GLFW_KEY_M -> Key.M
            GLFW.GLFW_KEY_N -> Key.N
            GLFW.GLFW_KEY_O -> Key.O
            GLFW.GLFW_KEY_P -> Key.P
            GLFW.GLFW_KEY_Q -> Key.Q
            GLFW.GLFW_KEY_R -> Key.R
            GLFW.GLFW_KEY_S -> Key.S
            GLFW.GLFW_KEY_T -> Key.T
            GLFW.GLFW_KEY_U -> Key.U
            GLFW.GLFW_KEY_V -> Key.V
            GLFW.GLFW_KEY_W -> Key.W
            GLFW.GLFW_KEY_X -> Key.X
            GLFW.GLFW_KEY_Y -> Key.Y
            GLFW.GLFW_KEY_Z -> Key.Z
            GLFW.GLFW_KEY_LEFT_BRACKET -> Key.LEFT_BRACKET
            GLFW.GLFW_KEY_BACKSLASH -> Key.BACKSLASH
            GLFW.GLFW_KEY_RIGHT_BRACKET -> Key.RIGHT_BRACKET
            GLFW.GLFW_KEY_GRAVE_ACCENT -> Key.GRAVE_ACCENT
            GLFW.GLFW_KEY_WORLD_1 -> Key.WORLD_1
            GLFW.GLFW_KEY_WORLD_2 -> Key.WORLD_2
            GLFW.GLFW_KEY_ESCAPE -> Key.ESCAPE
            GLFW.GLFW_KEY_ENTER -> Key.ENTER
            GLFW.GLFW_KEY_TAB -> Key.TAB
            GLFW.GLFW_KEY_BACKSPACE -> Key.BACKSPACE
            GLFW.GLFW_KEY_INSERT -> Key.INSERT
            GLFW.GLFW_KEY_DELETE -> Key.DELETE
            GLFW.GLFW_KEY_RIGHT -> Key.RIGHT
            GLFW.GLFW_KEY_LEFT -> Key.LEFT
            GLFW.GLFW_KEY_DOWN -> Key.DOWN
            GLFW.GLFW_KEY_UP -> Key.UP
            GLFW.GLFW_KEY_PAGE_UP -> Key.PAGE_UP
            GLFW.GLFW_KEY_PAGE_DOWN -> Key.PAGE_DOWN
            GLFW.GLFW_KEY_HOME -> Key.HOME
            GLFW.GLFW_KEY_END -> Key.END
            GLFW.GLFW_KEY_CAPS_LOCK -> Key.CAPS_LOCK
            GLFW.GLFW_KEY_SCROLL_LOCK -> Key.SCROLL_LOCK
            GLFW.GLFW_KEY_NUM_LOCK -> Key.NUM_LOCK
            GLFW.GLFW_KEY_PRINT_SCREEN -> Key.PRINT_SCREEN
            GLFW.GLFW_KEY_PAUSE -> Key.PAUSE
            GLFW.GLFW_KEY_F1 -> Key.F1
            GLFW.GLFW_KEY_F2 -> Key.F2
            GLFW.GLFW_KEY_F3 -> Key.F3
            GLFW.GLFW_KEY_F4 -> Key.F4
            GLFW.GLFW_KEY_F5 -> Key.F5
            GLFW.GLFW_KEY_F6 -> Key.F6
            GLFW.GLFW_KEY_F7 -> Key.F7
            GLFW.GLFW_KEY_F8 -> Key.F8
            GLFW.GLFW_KEY_F9 -> Key.F9
            GLFW.GLFW_KEY_F10 -> Key.F10
            GLFW.GLFW_KEY_F11 -> Key.F11
            GLFW.GLFW_KEY_F12 -> Key.F12
            GLFW.GLFW_KEY_F13 -> Key.F13
            GLFW.GLFW_KEY_F14 -> Key.F14
            GLFW.GLFW_KEY_F15 -> Key.F15
            GLFW.GLFW_KEY_F16 -> Key.F16
            GLFW.GLFW_KEY_F17 -> Key.F17
            GLFW.GLFW_KEY_F18 -> Key.F18
            GLFW.GLFW_KEY_F19 -> Key.F19
            GLFW.GLFW_KEY_F20 -> Key.F20
            GLFW.GLFW_KEY_F21 -> Key.F21
            GLFW.GLFW_KEY_F22 -> Key.F22
            GLFW.GLFW_KEY_F23 -> Key.F23
            GLFW.GLFW_KEY_F24 -> Key.F24
            GLFW.GLFW_KEY_F25 -> Key.F25
            GLFW.GLFW_KEY_KP_0 -> Key.KP_0
            GLFW.GLFW_KEY_KP_1 -> Key.KP_1
            GLFW.GLFW_KEY_KP_2 -> Key.KP_2
            GLFW.GLFW_KEY_KP_3 -> Key.KP_3
            GLFW.GLFW_KEY_KP_4 -> Key.KP_4
            GLFW.GLFW_KEY_KP_5 -> Key.KP_5
            GLFW.GLFW_KEY_KP_6 -> Key.KP_6
            GLFW.GLFW_KEY_KP_7 -> Key.KP_7
            GLFW.GLFW_KEY_KP_8 -> Key.KP_8
            GLFW.GLFW_KEY_KP_9 -> Key.KP_9
            GLFW.GLFW_KEY_KP_DECIMAL -> Key.KP_DECIMAL
            GLFW.GLFW_KEY_KP_DIVIDE -> Key.KP_DIVIDE
            GLFW.GLFW_KEY_KP_MULTIPLY -> Key.KP_MULTIPLY
            GLFW.GLFW_KEY_KP_SUBTRACT -> Key.KP_SUBTRACT
            GLFW.GLFW_KEY_KP_ADD -> Key.KP_ADD
            GLFW.GLFW_KEY_KP_ENTER -> Key.KP_ENTER
            GLFW.GLFW_KEY_KP_EQUAL -> Key.KP_EQUAL
            GLFW.GLFW_KEY_LEFT_SHIFT -> Key.LEFT_SHIFT
            GLFW.GLFW_KEY_LEFT_CONTROL -> Key.LEFT_CONTROL
            GLFW.GLFW_KEY_LEFT_ALT -> Key.LEFT_ALT
            GLFW.GLFW_KEY_LEFT_SUPER -> Key.LEFT_SUPER
            GLFW.GLFW_KEY_RIGHT_SHIFT -> Key.RIGHT_SHIFT
            GLFW.GLFW_KEY_RIGHT_CONTROL -> Key.RIGHT_CONTROL
            GLFW.GLFW_KEY_RIGHT_ALT -> Key.RIGHT_ALT
            GLFW.GLFW_KEY_RIGHT_SUPER -> Key.RIGHT_SUPER
            GLFW.GLFW_KEY_MENU -> Key.MENU
            else -> null
        }

        if (action == GLFW.GLFW_PRESS) {
            keyboard.setKeyState(convertedKey!!, true)
        }
        else if (action == GLFW.GLFW_RELEASE) {
            keyboard.setKeyState(convertedKey!!, false)
        }
    }

    fun swapBuffers() {
        keyboard.swapBuffers()
    }
}

interface Mouse {
    val x: Double
    val y: Double
    val deltaX: Double
    val deltaY: Double
    fun isButtonDown(button: MouseButton): Boolean
    fun isButtonUp(button: MouseButton): Boolean
    fun isButtonPressed(button: MouseButton): Boolean
    fun isButtonReleased(button: MouseButton): Boolean
}

class MutableMouse: Mouse {
    private val writeBuffer: MouseBuffer = MouseBuffer()
    private val readBuffers: Array<MouseBuffer> = Array(2) { MouseBuffer() }
    @Volatile
    private var swapIndex: Int = 0

    private val currentBuffer get() = readBuffers[swapIndex and 1]
    private val nextBuffer get() = readBuffers[(swapIndex + 1) and 1]
    private val buttonState get() = currentBuffer.buttonState
    private val prevButtonState get() = currentBuffer.prevButtonState

    override val x: Double get() = currentBuffer.x
    override val y: Double get() = currentBuffer.y
    override val deltaX: Double get() = currentBuffer.x - currentBuffer.prevX
    override val deltaY: Double get() = currentBuffer.y - currentBuffer.prevY
    override fun isButtonDown(button: MouseButton): Boolean = buttonState[button.ordinal]
    override fun isButtonUp(button: MouseButton): Boolean = !buttonState[button.ordinal]
    override fun isButtonPressed(button: MouseButton): Boolean =
        buttonState[button.ordinal] && !prevButtonState[button.ordinal]
    override fun isButtonReleased(button: MouseButton): Boolean =
        !buttonState[button.ordinal] && prevButtonState[button.ordinal]

    fun setCursorPosition(x: Double, y: Double) {
        writeBuffer.prevX = writeBuffer.x
        writeBuffer.prevY = writeBuffer.y
        writeBuffer.x = x
        writeBuffer.y = y
    }

    fun setButtonState(button: MouseButton, pressed: Boolean) {
        writeBuffer.prevButtonState[button.ordinal] = writeBuffer.buttonState[button.ordinal]
        writeBuffer.buttonState[button.ordinal] = pressed
    }

    fun swapBuffers() {
        nextBuffer.x = writeBuffer.x
        nextBuffer.y = writeBuffer.y
        nextBuffer.prevX = writeBuffer.prevX
        nextBuffer.prevY = writeBuffer.prevY
        writeBuffer.buttonState.copyInto(nextBuffer.buttonState)
        writeBuffer.prevButtonState.copyInto(nextBuffer.prevButtonState)
        swapIndex++
    }

    private class MouseBuffer {
        var x: Double = 0.0
        var y: Double = 0.0
        var prevX: Double = 0.0
        var prevY: Double = 0.0
        val buttonState: BooleanArray = BooleanArray(MouseButton.cachedValues.size)
        val prevButtonState: BooleanArray = BooleanArray(MouseButton.cachedValues.size)
    }
}

class GlfwMouseAdapter(
    private val window: Long,
    private val mouse: MutableMouse
): GLFWCursorPosCallbackI {
    private var cursorX = 0.0
    private var cursorY = 0.0

    init {
        GLFW.glfwSetInputMode(window, GLFW.GLFW_STICKY_MOUSE_BUTTONS, GLFW.GLFW_TRUE)
        GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED)
    }

    fun swapBuffers() {
        mouse.setCursorPosition(cursorX, cursorY)
        mouse.setButtonState(MouseButton.LMB, GLFW.glfwGetMouseButton(window, GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS)
        mouse.setButtonState(MouseButton.RMB, GLFW.glfwGetMouseButton(window, GLFW.GLFW_MOUSE_BUTTON_RIGHT) == GLFW.GLFW_PRESS)
        mouse.swapBuffers()
    }

    override fun invoke(window: Long, x: Double, y: Double) {
        cursorX = x
        cursorY = y
    }
}

enum class Key {
    SPACE,
    APOSTROPHE,
    COMMA,
    MINUS,
    PERIOD,
    SLASH,
    _0, _1, _2, _3, _4, _5, _6, _7, _8, _9,
    SEMICOLON,
    EQUAL,
    A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z,
    LEFT_BRACKET,
    BACKSLASH,
    RIGHT_BRACKET,
    GRAVE_ACCENT,
    WORLD_1,
    WORLD_2,
    ESCAPE,
    ENTER,
    TAB,
    BACKSPACE,
    INSERT,
    DELETE,
    RIGHT,
    LEFT,
    DOWN,
    UP,
    PAGE_UP,
    PAGE_DOWN,
    HOME,
    END,
    CAPS_LOCK,
    SCROLL_LOCK,
    NUM_LOCK,
    PRINT_SCREEN,
    PAUSE,
    F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, F11, F12, F13, F14, F15, F16, F17, F18, F19, F20, F21, F22, F23, F24, F25,
    KP_0, KP_1, KP_2, KP_3, KP_4, KP_5, KP_6, KP_7, KP_8, KP_9,
    KP_DECIMAL,
    KP_DIVIDE,
    KP_MULTIPLY,
    KP_SUBTRACT,
    KP_ADD,
    KP_ENTER,
    KP_EQUAL,
    LEFT_SHIFT,
    LEFT_CONTROL,
    LEFT_ALT,
    LEFT_SUPER,
    RIGHT_SHIFT,
    RIGHT_CONTROL,
    RIGHT_ALT,
    RIGHT_SUPER,
    MENU;

    companion object {
        val cachedValues = values()
    }
}

enum class MouseButton {
    LMB,
    RMB;

    companion object {
        val cachedValues = values()
    }
}
