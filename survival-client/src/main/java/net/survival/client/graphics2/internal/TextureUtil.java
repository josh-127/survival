package net.survival.client.graphics2.internal;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * A utility used to create textures.
 */
public final class TextureUtil
{
    private final int width;
    private final int height;
    private final int[] pixels;

    private TextureUtil(int width, int height) {
        this.width = width;
        this.height = height;
        pixels = new int[width * height];
    }

    private static TextureUtil loadBitmap(File file) throws IOException {
        BufferedImage loaded = ImageIO.read(file);
        TextureUtil bitmap = new TextureUtil(loaded.getWidth(), loaded.getHeight());

        for (int i = 0; i < bitmap.height; i++) {
            for (int j = 0; j < bitmap.width; j++)
                bitmap.pixels[j + i * bitmap.width] = loaded.getRGB(j, i);
        }

        return bitmap;
    }

    /**
     * Creates a black 1x1 texture.
     * 
     * @param factory the GraphicsResourceFactory used to create the texture
     * @return a black 1x1 texture
     */
    public static Texture createEmpty(GraphicsResourceFactory factory) {
        Rgba8Texture texture = factory.createRgba8Texture(1, 1, false);
        texture.writeTexels(new int[] { 0xFFFFFFFF });
        return texture;
    }

    /**
     * Loads an image asset as a texture.
     * 
     * @param loader  the raw asset loader used to load the image
     * @param factory the GraphicsResourceFactory used to create the texture
     * @param asset   the asset to load
     * @return a texture that is loaded from the image asset
     */
    public static Texture load(File file, GraphicsResourceFactory factory) {
        try {
            TextureUtil bitmap = loadBitmap(file);
            Rgba8Texture texture = factory.createRgba8Texture(bitmap.width, bitmap.height, true);
            texture.writeTexels(bitmap.pixels);
            return texture;
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}