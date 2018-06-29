package net.survival.client.graphics.gui;

import java.util.HashMap;

import net.survival.client.graphics.opengl.GLImmediateDrawCall;
import net.survival.client.gui.Font;

class FontRenderer
{
    private static final HashMap<Font, CharacterSheet> characterSheets = new HashMap<>();
    
    public static void drawText(Font font, String text) {
        CharacterSheet characterSheet = characterSheets.get(font);
        if (characterSheet == null) {
            characterSheet = new CharacterSheet(font.characterSheetPath);
            characterSheets.put(font, characterSheet);
        }
        
        float cursorX = 0.0f;
        float cursorY = 0.0f;
        
        GLImmediateDrawCall drawCall = GLImmediateDrawCall.beginTriangles(characterSheet.texture);
        drawCall.color(1.0f, 1.0f, 1.0f);
        
        for (int i = 0; i < text.length(); ++i) {
            char c = text.charAt(i);
            float u1 = characterSheet.getTexCoordU1(c);
            float v1 = characterSheet.getTexCoordV1(c);
            float u2 = characterSheet.getTexCoordU2(c);
            float v2 = characterSheet.getTexCoordV2(c);
            
            drawCall.texturedVertex(cursorX,        cursorY + 1.0f, 0.0f, u1, v1);
            drawCall.texturedVertex(cursorX + 1.0f, cursorY + 1.0f, 0.0f, u2, v1);
            drawCall.texturedVertex(cursorX + 1.0f, cursorY,        0.0f, u2, v2);
            drawCall.texturedVertex(cursorX + 1.0f, cursorY,        0.0f, u2, v2);
            drawCall.texturedVertex(cursorX,        cursorY,        0.0f, u1, v2);
            drawCall.texturedVertex(cursorX,        cursorY + 1.0f, 0.0f, u1, v1);
            
            cursorX += 1.0f;
        }
        
        drawCall.end();
    }
}