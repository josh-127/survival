package net.survival.client.graphics;

import net.survival.client.graphics.opengl.GLImmediateDrawCall;

class FontRenderer implements GraphicsResource
{
    private final FontTextureAtlas fontTextureAtlas;

    public FontRenderer() {
        fontTextureAtlas = new FontTextureAtlas();
    }

    @Override
    public void close() {
        fontTextureAtlas.close();
    }

    public void drawText(String text, float x, float y, float z, float scaleX, float scaleY) {
        var cursorX = x;
        var fontWidth = fontTextureAtlas.fontWidth * scaleX;
        var fontHeight = fontTextureAtlas.fontHeight * scaleY;

        var drawCall = GLImmediateDrawCall.beginTriangles(fontTextureAtlas.characters);
        drawCall.color(1.0f, 1.0f, 1.0f);

        for (var i = 0; i < text.length(); ++i) {
            var c = text.charAt(i);
            var u1 = fontTextureAtlas.getTexCoordU1(c);
            var v1 = fontTextureAtlas.getTexCoordV1(c);
            var u2 = fontTextureAtlas.getTexCoordU2(c);
            var v2 = fontTextureAtlas.getTexCoordV2(c);

            drawCall.texturedVertex(cursorX,             y + fontHeight, z, u1, v2);
            drawCall.texturedVertex(cursorX + fontWidth, y + fontHeight, z, u2, v2);
            drawCall.texturedVertex(cursorX + fontWidth, y,              z, u2, v1);
            drawCall.texturedVertex(cursorX + fontWidth, y,              z, u2, v1);
            drawCall.texturedVertex(cursorX,             y,              z, u1, v1);
            drawCall.texturedVertex(cursorX,             y + fontHeight, z, u1, v2);

            cursorX += fontWidth;
        }

        drawCall.end();
    }
}