package net.survival.graphics

private const val DEFAULT_RED = 1.0f
private const val DEFAULT_GREEN = 1.0f
private const val DEFAULT_BLUE = 1.0f
private const val DEFAULT_ALPHA = 1.0f
private const val DEFAULT_FONT_SIZE = 1.0f
private const val DEFAULT_HORIZONTAL_SPACING = 0.125f
private const val DEFAULT_VERTICAL_SPACING = 0.25f
private const val DEFAULT_SPACE_WIDTH = 0.5f
private const val DEFAULT_TAB_WIDTH = 2.0f

class TextStyle private constructor(
    val red: Float,
    val green: Float,
    val blue: Float,
    val alpha: Float,
    val fontSize: Float,
    val horizontalSpacing: Float,
    val verticalSpacing: Float,
    val spaceWidth: Float,
    val tabWidth: Float
) {
    companion object {
        val DEFAULT = TextStyle(
            DEFAULT_RED,
            DEFAULT_GREEN,
            DEFAULT_BLUE,
            DEFAULT_ALPHA,
            DEFAULT_FONT_SIZE,
            DEFAULT_HORIZONTAL_SPACING,
            DEFAULT_VERTICAL_SPACING,
            DEFAULT_SPACE_WIDTH,
            DEFAULT_TAB_WIDTH
        )
    }
}