package net.survival.client.graphics2.internal;

import java.io.File;

/**
 * A font defines the appearance of text.
 */
public class Font
{
    public final Texture texture;
    public final int[] charWidths;
    public final int charHeight;

    private Font(Texture texture, int[] charWidths, int charHeight) {
        this.texture = texture;
        this.charWidths = charWidths;
        this.charHeight = charHeight;
    }

    /**
     * Loads a font.
     * 
     * @param loader     the RawAssetLoader used to load the font
     * @param factory    the GraphicsResourceFactory used to create the font
     * @param asset      the font asset to load
     * @param charWidths the character widths of the font
     * @param charHeight the height of each character in the font
     * @return the loaded font
     */
    public static Font load(
            File file,
            GraphicsResourceFactory factory,
            int[] charWidths,
            int charHeight)
    {
        return new Font(TextureUtil.load(file, factory), charWidths, charHeight);
    }
}