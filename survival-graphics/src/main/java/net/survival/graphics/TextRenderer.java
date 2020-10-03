package net.survival.graphics;

import java.nio.file.Paths;

import net.survival.graphics.opengl.GLBlendFactor;
import net.survival.graphics.opengl.GLComparisonFunc;
import net.survival.graphics.opengl.GLImmediateDrawCall;
import net.survival.graphics.opengl.GLState;

class TextRenderer {
    private final String[] texturePaths;

    public TextRenderer(String fontPath) {
        texturePaths = new String[128];

        for (var i = 0; i < texturePaths.length; ++i) {
            var fileName = Integer.toString(i) + ".png";
            texturePaths[i] = Paths.get(fontPath, fileName).toString();
        }
    }

    public void drawText(
            String text,
            TextStyle textStyle,
            double x,
            double y,
            double z)
    {
        var textureAtlas = Assets.getTextureAtlas();
        var textureObject = textureAtlas.getTextureObject();

        if (textStyle.getAlpha() == 1.0) {
            GLState.pushAlphaTestEnabled(true);
            GLState.pushAlphaFunction(GLComparisonFunc.EQUAL, 1.0f);
        }
        else {
            GLState.pushBlendEnabled(true);
            GLState.pushBlendFunction(GLBlendFactor.SRC_ALPHA, GLBlendFactor.ONE_MINUS_SRC_ALPHA);
        }

        var drawCall = GLImmediateDrawCall.beginTriangles(textureObject);
        drawCall.color(
                (float) textStyle.getRed(),
                (float) textStyle.getGreen(),
                (float) textStyle.getBlue(),
                (float) textStyle.getAlpha());

        var lineHeight = getLineHeight(textStyle);
        var cursorX = x;
        var cursorY = y;

        for (var i = 0; i < text.length(); ++i) {
            var c = text.charAt(i);

            if (c == ' ') {
                cursorX += textStyle.getSpaceWidth();
            }
            else if (c == '\t') {
                cursorX += textStyle.getTabWidth();
            }
            else if (c == '\r') {
                cursorX = x;
            }
            else if (c == '\n') {
                cursorX = x;
                cursorY += lineHeight;
            }
            else {
                cursorX = putChar(drawCall, c, textStyle, cursorX, cursorY, z);
            }
        }

        drawCall.end();

        if (textStyle.getAlpha() == 1.0) {
            GLState.popAlphaTestEnabled();
            GLState.popAlphaFunction();
        }
        else {
            GLState.popBlendEnabled();
            GLState.popBlendFunction();
        }
    }

    public void drawTextBounded(
            String text,
            TextStyle textStyle,
            double left,
            double top,
            double right,
            double bottom,
            double depth)
    {
        var textureAtlas = Assets.getTextureAtlas();
        var textureObject = textureAtlas.getTextureObject();

        if (textStyle.getAlpha() == 1.0) {
            GLState.pushAlphaTestEnabled(true);
            GLState.pushAlphaFunction(GLComparisonFunc.EQUAL, 1.0f);
        }
        else {
            GLState.pushBlendEnabled(true);
            GLState.pushBlendFunction(GLBlendFactor.SRC_ALPHA, GLBlendFactor.ONE_MINUS_SRC_ALPHA);
        }

        var drawCall = GLImmediateDrawCall.beginTriangles(textureObject);
        drawCall.color(
                (float) textStyle.getRed(),
                (float) textStyle.getGreen(),
                (float) textStyle.getBlue(),
                (float) textStyle.getAlpha());

        var lineHeight = getLineHeight(textStyle);
        var cursorX = left;
        var cursorY = top;

        var textWindow = text;

        while (!textWindow.isEmpty()) {
            var splitIndexAndIsWhitespace = shift(textWindow);
            var splitIndex = Math.abs(splitIndexAndIsWhitespace);
            var isWhitespace = splitIndexAndIsWhitespace < 0;

            var part = textWindow.substring(0, splitIndex);
            textWindow = textWindow.substring(splitIndex);

            if (isWhitespace) {
                for (var i = 0; i < part.length(); ++i) {
                    var c = part.charAt(i);

                    if (c == ' ') {
                        cursorX += textStyle.getSpaceWidth();
                    }
                    else if (c == '\t') {
                        cursorX += textStyle.getTabWidth();
                    }
                    else if (c == '\r') {
                        cursorX = left;
                    }
                    else if (c == '\n') {
                        cursorX = left;
                        cursorY += lineHeight;
                    }
                    else {
                        throw new RuntimeException("Assertion failure: isWhitespace(c)");
                    }
                }
            }
            else {
                var width = getTextWidth(part, textStyle);

                if (cursorX + width >= right) {
                    cursorX = left;
                    cursorY += lineHeight;
                }

                if (cursorY + lineHeight >= bottom) {
                    break;
                }

                putWord(drawCall, part, textStyle, cursorX, cursorY, depth);
                cursorX += width;
            }
        }

        drawCall.end();

        if (textStyle.getAlpha() == 1.0) {
            GLState.popAlphaFunction();
            GLState.popAlphaTestEnabled();
        }
        else {
            GLState.popBlendEnabled();
            GLState.popBlendFunction();
        }
    }

    private void putWord(
            GLImmediateDrawCall drawCall,
            String text,
            TextStyle textStyle,
            double x,
            double y,
            double z)
    {
        var cursorX = x;
        var cursorY = y;

        for (var i = 0; i < text.length(); ++i) {
            var c = text.charAt(i);
            cursorX = putChar(drawCall, c, textStyle, cursorX, cursorY, z);
        }
    }

    private double putChar(
            GLImmediateDrawCall drawCall,
            char character,
            TextStyle textStyle,
            double x,
            double y,
            double z)
    {
        var textureAtlas = Assets.getTextureAtlas();
        var path = texturePaths[(int) character];

        var pixelHeight = (double) textureAtlas.getHeight(path);
        var emWidth = (double) textureAtlas.getWidth(path) / pixelHeight;
        emWidth *= textStyle.getFontSize();

        var u1 = textureAtlas.getTexCoordU1(path);
        var v1 = textureAtlas.getTexCoordV1(path);
        var u2 = textureAtlas.getTexCoordU2(path);
        var v2 = textureAtlas.getTexCoordV2(path);

        var left = (float) x;
        var right = (float) (x + emWidth);
        var top = (float) y;
        var bottom = (float) (y + textStyle.getFontSize());
        drawCall.texturedVertex(left,  bottom, (float) z, u1, v1);
        drawCall.texturedVertex(right, bottom, (float) z, u2, v1);
        drawCall.texturedVertex(right, top,    (float) z, u2, v2);
        drawCall.texturedVertex(right, top,    (float) z, u2, v2);
        drawCall.texturedVertex(left,  top,    (float) z, u1, v2);
        drawCall.texturedVertex(left,  bottom, (float) z, u1, v1);

        return x + emWidth + textStyle.getHorizontalSpacing();
    }

    private double getTextWidth(String text, TextStyle textStyle) {
        var totalWidth = 0.0;
        var textureAtlas = Assets.getTextureAtlas();

        for (var i = 0; i < text.length(); ++i) {
            var c = text.charAt(i);

            if (c == ' ') {
                totalWidth += textStyle.getSpaceWidth();
            }
            else if (c == '\t') {
                totalWidth += textStyle.getTabWidth();
            }
            else {
                var path = texturePaths[(int) c];
                var pixelHeight = (double) textureAtlas.getHeight(path);
                var emWidth = (double) textureAtlas.getWidth(path) / pixelHeight;
                emWidth *= textStyle.getFontSize();

                totalWidth += emWidth + textStyle.getHorizontalSpacing();
            }
        }

        return totalWidth;
    }

    private int shift(String text) {
        if (text.isEmpty()) {
            return 0;
        }

        var firstChar = text.charAt(0);

        if (isWhitespace(firstChar)) {
            for (var i = 0; i < text.length(); ++i) {
                var c = text.charAt(i);

                if (!isWhitespace(c)) {
                    return -i;
                }
            }
        }
        else {
            for (var i = 0; i < text.length(); ++i) {
                var c = text.charAt(i);

                if (isWhitespace(c)) {
                    return i;
                }
            }
        }

        return text.length();
    }

    private static boolean isWhitespace(char c) {
        return c == ' ' || c == '\t' || c == '\r' || c == '\n';
    }

    private double getLineHeight(TextStyle textStyle) {
        var fontSize = textStyle.getFontSize();
        var verticalSpacing = textStyle.getVerticalSpacing();
        return fontSize * (1.0 + verticalSpacing);
    }
}