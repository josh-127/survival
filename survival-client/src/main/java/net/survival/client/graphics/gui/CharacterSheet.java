package net.survival.client.graphics.gui;

import net.survival.client.graphics.Bitmap;
import net.survival.client.graphics.GraphicsResource;
import net.survival.client.graphics.opengl.GLFilterMode;
import net.survival.client.graphics.opengl.GLTexture;
import net.survival.client.graphics.opengl.GLWrapMode;

class CharacterSheet implements GraphicsResource
{
    public final GLTexture texture;
    
    public CharacterSheet(String filePath) {
        Bitmap bitmap = Bitmap.fromFile(filePath);
        texture = new GLTexture();
        
        texture.beginBind()
            .setMipmapEnabled(false)
            .setData(bitmap)
            .setMinFilter(GLFilterMode.NEAREST)
            .setMagFilter(GLFilterMode.NEAREST)
            .setWrapS(GLWrapMode.REPEAT)
            .setWrapT(GLWrapMode.REPEAT)
            .endBind();
    }
    
    @Override
    public void close() {
        texture.close();
    }
    
    public float getTexCoordU1(char c) {
        return (c % 16) / 16.0f;
    }

    public float getTexCoordV1(char c) {
        return getTexCoordV2(c) + (1.0f / 16.0f);
    }

    public float getTexCoordU2(char c) {
        return getTexCoordU1(c) + (1.0f / 16.0f);
    }

    public float getTexCoordV2(char c) {
        return (c / 16) / 16.0f;
    }
}