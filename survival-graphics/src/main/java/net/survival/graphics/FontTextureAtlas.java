package net.survival.graphics;

import java.nio.file.Paths;

import net.survival.graphics.opengl.GLFilterMode;
import net.survival.graphics.opengl.GLTexture;
import net.survival.graphics.opengl.GLWrapMode;

// TODO: Make loading non-blocking.
class FontTextureAtlas implements GraphicsResource
{
    private GLTexture textureAtlas;
    private int fontWidth;
    private int fontHeight;

    public FontTextureAtlas() {}

    public void setFontPath(String path) {
        var fullPath = Paths.get(GraphicsSettings.ASSET_ROOT_PATH, path);
        var bitmap = Bitmap.fromFile(fullPath.toString());

        if (textureAtlas != null) {
            textureAtlas.close();
        }

        textureAtlas = new GLTexture();
        textureAtlas.beginBind()
                .setMipmapEnabled(false)
                .setData(bitmap)
                .setMinFilter(GLFilterMode.NEAREST)
                .setMagFilter(GLFilterMode.NEAREST)
                .setWrapS(GLWrapMode.REPEAT)
                .setWrapT(GLWrapMode.REPEAT)
                .endBind();

        fontWidth = bitmap.getWidth() / 16;
        fontHeight = bitmap.getHeight() / 16;
    }

    @Override
    public void close() {
        textureAtlas.close();
    }

    public GLTexture getTextureAtlas() {
        return textureAtlas;
    }

    public int getFontWidth() {
        return fontWidth;
    }

    public int getFontHeight() {
        return fontHeight;
    }

    public float getTexCoordU1(char c) {
        return (c % 16) / 16.0f;
    }

    public float getTexCoordV1(char c) {
        return (c / 16) / 16.0f;
    }

    public float getTexCoordU2(char c) {
        return getTexCoordU1(c) + (1.0f / 16.0f);
    }

    public float getTexCoordV2(char c) {
        return getTexCoordV1(c) + (1.0f / 16.0f);
    }
}