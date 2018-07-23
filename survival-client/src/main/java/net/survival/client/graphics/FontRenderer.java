package net.survival.client.graphics;

import net.survival.client.graphics.opengl.GLImmediateDrawCall;

public class FontRenderer implements GraphicsResource
{
    private final FontTextureAtlas fontTextureAtlas;

    public FontRenderer() {
        fontTextureAtlas = new FontTextureAtlas();
    }

    @Override
    public void close() {
        fontTextureAtlas.close();
    }

    public void drawText(String text, float x, float y, float z) {
        float cursorX = x;

        GLImmediateDrawCall drawCall = GLImmediateDrawCall
                .beginTriangles(fontTextureAtlas.characters);
        drawCall.color(1.0f, 1.0f, 1.0f);

        for (int i = 0; i < text.length(); ++i) {
            char c = text.charAt(i);
            float u1 = fontTextureAtlas.getTexCoordU1(c);
            float v1 = fontTextureAtlas.getTexCoordV1(c);
            float u2 = fontTextureAtlas.getTexCoordU2(c);
            float v2 = fontTextureAtlas.getTexCoordV2(c);

            drawCall.texturedVertex(cursorX,        y,        z, u1, v1);
            drawCall.texturedVertex(cursorX + 1.0f, y,        z, u2, v1);
            drawCall.texturedVertex(cursorX + 1.0f, y + 1.0f, z, u2, v2);
            drawCall.texturedVertex(cursorX + 1.0f, y + 1.0f, z, u2, v2);
            drawCall.texturedVertex(cursorX,        y + 1.0f, z, u1, v2);
            drawCall.texturedVertex(cursorX,        y,        z, u1, v1);

            cursorX += 1.0f;
        }

        drawCall.end();
    }
}