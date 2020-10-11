package net.survival.graphics

private const val DEFAULT_RED = 1.0
private const val DEFAULT_GREEN = 1.0
private const val DEFAULT_BLUE = 1.0
private const val DEFAULT_ALPHA = 1.0
private const val DEFAULT_FONT_SIZE = 1.0
private const val DEFAULT_HORIZONTAL_SPACING = 0.125
private const val DEFAULT_VERTICAL_SPACING = 0.25
private const val DEFAULT_SPACE_WIDTH = 0.5
private const val DEFAULT_TAB_WIDTH = 2.0

class TextStyle private constructor(
    val red: Double,
    val green: Double,
    val blue: Double,
    val alpha: Double,
    val fontSize: Double,
    val horizontalSpacing: Double,
    val verticalSpacing: Double,
    val spaceWidth: Double,
    val tabWidth: Double
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