package net.survival.client.graphics;

import net.survival.client.graphics.opengl.GLFilterMode;
import net.survival.client.graphics.opengl.GLTexture;
import net.survival.client.graphics.opengl.GLWrapMode;

// TODO: Remove hard-coding
// TODO: Make loading non-blocking
class FontTextureAtlas implements GraphicsResource
{
    public final GLTexture characters;

    public FontTextureAtlas() {
        Bitmap bitmap = Bitmap.fromFile("../assets/textures/fonts/ascii.png");
        characters = new GLTexture();

        characters.beginBind()
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
        characters.close();
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