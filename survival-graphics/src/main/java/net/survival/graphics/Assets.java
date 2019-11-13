package net.survival.graphics;

public final class Assets
{
    private static TextureAtlas mipmappedTextureAtlas;
    private static TextureAtlas textureAtlas;
    private static boolean initialized;

    private Assets() {}

    public static void setup(TextureAtlas mipmappedTextureAtlas, TextureAtlas textureAtlas) {
        if (initialized) {
            throw new IllegalStateException("Assets have already been initialized.");
        }

        Assets.mipmappedTextureAtlas = mipmappedTextureAtlas;
        Assets.textureAtlas = textureAtlas;
        initialized = true;
    }

    public static void tearDown() {
        if (!initialized) {
            throw new IllegalStateException("Assets have already been torn down.");
        }

        mipmappedTextureAtlas.close();
        textureAtlas.close();
        initialized = false;
    }

    public static TextureAtlas getMipmappedTextureAtlas() {
        if (mipmappedTextureAtlas == null) {
            throw new IllegalStateException("mipmappedTextureAtlas has not been initialized.");
        }

        return mipmappedTextureAtlas;
    }

    public static TextureAtlas getTextureAtlas() {
        if (textureAtlas == null) {
            throw new IllegalStateException("textureAtlas has not been initialized.");
        }

        return textureAtlas;
    }
}