package net.survival.graphics

import net.survival.graphics.opengl.GLBlendFactor
import net.survival.graphics.opengl.GLComparisonFunc
import net.survival.graphics.opengl.GLImmediateDrawCall
import net.survival.graphics.opengl.GLState
import java.nio.file.Paths
import kotlin.math.abs

internal class TextRenderer(fontPath: String) {
    private val texturePaths: Array<String?> = arrayOfNulls(128)

    init {
        for (i in texturePaths.indices) {
            val fileName = "$i.png"
            texturePaths[i] = Paths.get(fontPath, fileName).toString()
        }
    }

    fun drawText(
        text: String,
        textStyle: TextStyle,
        x: Float,
        y: Float,
        z: Float
    ) {
        val textureAtlas = Assets.getTextureAtlas()
        val textureObject = textureAtlas.textureObject

        if (textStyle.alpha == 1.0f) {
            GLState.pushAlphaTestEnabled(true)
            GLState.pushAlphaFunction(GLComparisonFunc.EQUAL, 1.0f)
        }
        else {
            GLState.pushBlendEnabled(true)
            GLState.pushBlendFunction(GLBlendFactor.SRC_ALPHA, GLBlendFactor.ONE_MINUS_SRC_ALPHA)
        }

        val drawCall = GLImmediateDrawCall.beginTriangles(textureObject)
        drawCall.color(
            textStyle.red,
            textStyle.green,
            textStyle.blue,
            textStyle.alpha
        )

        val lineHeight = getLineHeight(textStyle)
        var cursorX = x
        var cursorY = y

        for (ch in text) {
            when (ch) {
                ' ' -> cursorX += textStyle.spaceWidth
                '\t' -> cursorX += textStyle.tabWidth
                '\r' -> cursorX = x
                '\n' -> {
                    cursorX = x
                    cursorY += lineHeight
                }
                else -> cursorX = putChar(drawCall, ch, textStyle, cursorX, cursorY, z)
            }
        }

        drawCall.end()

        if (textStyle.alpha == 1.0f) {
            GLState.popAlphaTestEnabled()
            GLState.popAlphaFunction()
        }
        else {
            GLState.popBlendEnabled()
            GLState.popBlendFunction()
        }
    }

    fun drawTextBounded(
        text: String,
        textStyle: TextStyle,
        left: Float,
        top: Float,
        right: Float,
        bottom: Float,
        depth: Float
    ) {
        val textureAtlas = Assets.getTextureAtlas()
        val textureObject = textureAtlas.textureObject

        if (textStyle.alpha == 1.0f) {
            GLState.pushAlphaTestEnabled(true)
            GLState.pushAlphaFunction(GLComparisonFunc.EQUAL, 1.0f)
        }
        else {
            GLState.pushBlendEnabled(true)
            GLState.pushBlendFunction(GLBlendFactor.SRC_ALPHA, GLBlendFactor.ONE_MINUS_SRC_ALPHA)
        }

        val drawCall = GLImmediateDrawCall.beginTriangles(textureObject)
        drawCall.color(
            textStyle.red,
            textStyle.green,
            textStyle.blue,
            textStyle.alpha
        )
        val lineHeight = getLineHeight(textStyle)
        var cursorX = left
        var cursorY = top

        var textWindow = text

        while (textWindow.isNotEmpty()) {
            val splitIndexAndIsWhitespace = shift(textWindow)
            val splitIndex = abs(splitIndexAndIsWhitespace)
            val isWhitespace = splitIndexAndIsWhitespace < 0

            val part = textWindow.substring(0, splitIndex)
            textWindow = textWindow.substring(splitIndex)

            if (isWhitespace) {
                for (ch in part) {
                    if (ch == ' ') {
                        cursorX += textStyle.spaceWidth
                    }
                    else if (ch == '\t') {
                        cursorX += textStyle.tabWidth
                    }
                    else if (ch == '\r') {
                        cursorX = left
                    }
                    else if (ch == '\n') {
                        cursorX = left
                        cursorY += lineHeight
                    }
                    else {
                        error("Assertion failure: isWhitespace(ch)")
                    }
                }
            }
            else {
                val width = getTextWidth(part, textStyle)

                if (cursorX + width >= right) {
                    cursorX = left
                    cursorY += lineHeight
                }

                if (cursorY + lineHeight >= bottom) {
                    break
                }

                putWord(drawCall, part, textStyle, cursorX, cursorY, depth)
                cursorX += width
            }
        }

        drawCall.end()

        if (textStyle.alpha == 1.0f) {
            GLState.popAlphaFunction()
            GLState.popAlphaTestEnabled()
        }
        else {
            GLState.popBlendEnabled()
            GLState.popBlendFunction()
        }
    }

    private fun putWord(
        drawCall: GLImmediateDrawCall,
        text: String,
        textStyle: TextStyle,
        x: Float,
        y: Float,
        z: Float
    ) {
        var cursorX = x
        for (ch in text) {
            cursorX = putChar(drawCall, ch, textStyle, cursorX, y, z)
        }
    }

    private fun putChar(
        drawCall: GLImmediateDrawCall,
        character: Char,
        textStyle: TextStyle,
        x: Float,
        y: Float,
        z: Float
    ): Float {
        val textureAtlas = Assets.getTextureAtlas()
        val path = texturePaths[character.toInt()]!!

        val pixelHeight = textureAtlas.getHeight(path).toFloat()
        var emWidth = textureAtlas.getWidth(path).toFloat() / pixelHeight
        emWidth *= textStyle.fontSize

        val u1 = textureAtlas.getTexCoordU1(path)
        val v1 = textureAtlas.getTexCoordV1(path)
        val u2 = textureAtlas.getTexCoordU2(path)
        val v2 = textureAtlas.getTexCoordV2(path)

        val left = x
        val right = (x + emWidth)
        val top = y
        val bottom = (y + textStyle.fontSize)
        drawCall.texturedVertex(left, bottom, z, u1, v1)
        drawCall.texturedVertex(right, bottom, z, u2, v1)
        drawCall.texturedVertex(right, top, z, u2, v2)
        drawCall.texturedVertex(right, top, z, u2, v2)
        drawCall.texturedVertex(left, top, z, u1, v2)
        drawCall.texturedVertex(left, bottom, z, u1, v1)

        return x + emWidth + textStyle.horizontalSpacing
    }

    private fun getTextWidth(text: String, textStyle: TextStyle): Float {
        var totalWidth = 0.0f
        val textureAtlas = Assets.getTextureAtlas()

        for (ch in text) {
            if (ch == ' ') {
                totalWidth += textStyle.spaceWidth
            }
            else if (ch == '\t') {
                totalWidth += textStyle.tabWidth
            }
            else {
                val path = texturePaths[ch.toInt()]!!
                val pixelHeight = textureAtlas.getHeight(path).toFloat()
                var emWidth = textureAtlas.getWidth(path).toFloat() / pixelHeight
                emWidth *= textStyle.fontSize
                totalWidth += emWidth + textStyle.horizontalSpacing
            }
        }

        return totalWidth
    }

    private fun shift(text: String): Int {
        if (text.isEmpty()) {
            return 0
        }

        val firstChar = text[0]

        if (isWhitespace(firstChar)) {
            for (i in text.indices) {
                val ch = text[i]
                if (!isWhitespace(ch)) {
                    return -i
                }
            }
        }
        else {
            for (i in text.indices) {
                val ch = text[i]
                if (isWhitespace(ch)) {
                    return i
                }
            }
        }

        return text.length
    }

    private fun getLineHeight(textStyle: TextStyle): Float {
        val fontSize = textStyle.fontSize
        val verticalSpacing = textStyle.verticalSpacing
        return fontSize * (1.0f + verticalSpacing)
    }
}

private fun isWhitespace(c: Char): Boolean {
    return c == ' ' || c == '\t' || c == '\r' || c == '\n'
}
