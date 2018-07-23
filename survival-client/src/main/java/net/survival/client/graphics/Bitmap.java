package net.survival.client.graphics;

import static org.lwjgl.stb.STBImage.*;

import java.nio.ByteBuffer;

public class Bitmap
{
    private int width;
    private int height;
    private int[] pixels;

    public Bitmap(int width, int height) {
        this.width = width;
        this.height = height;
        pixels = new int[width * height];
    }

    private Bitmap(int width, int height, int[] pixels) {
        this.width = width;
        this.height = height;
        this.pixels = pixels;
    }

    private static final int[] stbi_load_w = new int[1];
    private static final int[] stbi_load_h = new int[1];
    private static final int[] stbi_load_comp = new int[1];

    public static Bitmap fromFile(String filePath) {
        ByteBuffer data = stbi_load(filePath, stbi_load_w, stbi_load_h, stbi_load_comp, 4);
        // TODO: Change this terrible ugly hack.
        if (data == null) {
            throw new RuntimeException(filePath);
        }
        int width = stbi_load_w[0];
        int height = stbi_load_h[0];
        int[] pixels = new int[width * height];
        data.asIntBuffer().get(pixels);
        stbi_image_free(data);

        return new Bitmap(width, height, pixels);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getPixel(int x, int y) {
        return pixels[x + (height - y - 1) * width];
    }

    public void setPixel(int x, int y, int to) {
        pixels[x + (height - y - 1) * width] = to;
    }

    public boolean isInBound(int x, int y) {
        return x >= 0 && y >= 0 && x < width && y < height;
    }

    public int[] getPixelArray() {
        return pixels;
    }

    public static void blit(Bitmap src, int srcX, int srcY, int srcW, int srcH, Bitmap dst,
            int dstX, int dstY)
    {
        for (int y = srcY; y < srcY + srcH; ++y) {
            int blitY = dstY + y;
            if (blitY < 0 || blitY >= dst.height)
                continue;

            for (int x = srcX; x < srcX + srcW; ++x) {
                int blitX = dstX + x;
                if (blitX < 0 || blitX >= dst.width)
                    continue;

                int srcPixel = src.getPixel(x, y);
                dst.setPixel(blitX, blitY, srcPixel);
            }
        }
    }
}