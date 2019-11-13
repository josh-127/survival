package net.survival.graphics;

import java.nio.file.Paths;
import java.util.HashMap;

import net.survival.graphics.opengl.GLFilterMode;
import net.survival.graphics.opengl.GLTexture;
import net.survival.graphics.opengl.GLWrapMode;

public class TextureAtlas implements GraphicsResource
{
    private static final int INITIAL_WIDTH = 64;
    private static final int INITIAL_HEIGHT = 64;

    private final GLTexture textureObject;
    private boolean needsUpdating;

    private BitmapAtlasBuilder atlasBuilder;
    private float inverseWidth;
    private float inverseHeight;
    private HashMap<String, BitmapRegion> texCoords;
    private HashMap<String, Integer> textureWidths;
    private HashMap<String, Integer> textureHeights;

    private final String rootPath;

    public TextureAtlas(String rootPath, boolean mipmappingEnabled) {
        textureObject = new GLTexture();

        if (mipmappingEnabled) {
            textureObject.beginBind()
                    .setMinFilter(GLFilterMode.NEAREST_MIPMAP_NEAREST)
                    .setMagFilter(GLFilterMode.NEAREST)
                    .setWrapS(GLWrapMode.REPEAT)
                    .setWrapT(GLWrapMode.REPEAT)
                    .setMipmapEnabled(true)
                    .setMinLod(0)
                    .setMaxLod(4)
                    .setData(new Bitmap(INITIAL_WIDTH, INITIAL_HEIGHT))
                    .endBind();
        }
        else {
            textureObject.beginBind()
                    .setMinFilter(GLFilterMode.NEAREST)
                    .setMagFilter(GLFilterMode.NEAREST)
                    .setWrapS(GLWrapMode.REPEAT)
                    .setWrapT(GLWrapMode.REPEAT)
                    .setData(new Bitmap(INITIAL_WIDTH, INITIAL_HEIGHT))
                    .endBind();
        }

        needsUpdating = false;

        atlasBuilder = new BitmapAtlasBuilder(INITIAL_WIDTH, INITIAL_HEIGHT);
        inverseWidth = 1.0f / atlasBuilder.getWidth();
        inverseHeight = 1.0f / atlasBuilder.getHeight();

        texCoords = new HashMap<>();
        textureWidths = new HashMap<>();
        textureHeights = new HashMap<>();

        this.rootPath = rootPath;
    }

    @Override
    public void close() {
        textureObject.close();
    }

    public GLTexture getTextureObject() {
        return textureObject;
    }

    public void updateTextures() {
        if (needsUpdating) {
            textureObject.beginBind()
                    .setData(atlasBuilder.build())
                    .endBind();

            needsUpdating = false;
        }
    }

    public int getWidth(String texture) {
        lazyLoadTexture(texture);
        return textureWidths.get(texture);
    }

    public int getHeight(String texture) {
        lazyLoadTexture(texture);
        return textureHeights.get(texture);
    }

    public float getTexCoordU1(String texture) {
        return (float) lazyLoadTexture(texture).getLeft() * inverseWidth;
    }

    public float getTexCoordV1(String texture) {
        return (float) 1.0f - lazyLoadTexture(texture).getTop() * inverseHeight;
    }

    public float getTexCoordU2(String texture) {
        return (float) lazyLoadTexture(texture).getRight() * inverseWidth;
    }

    public float getTexCoordV2(String texture) {
        return (float) 1.0f - lazyLoadTexture(texture).getBottom() * inverseHeight;
    }

    private BitmapRegion lazyLoadTexture(String texture) {
        var region = texCoords.get(texture);
        
        while (region == null) {
            var fullPath = Paths.get(rootPath, texture).toString();
            var bitmap = Bitmap.fromFile(fullPath);
            region = atlasBuilder.addBitmap(bitmap);
            
            if (region == null) {
                rebuildAtlas();
            }
            else {
                texCoords.put(texture, region);
                textureWidths.put(texture, bitmap.getWidth());
                textureHeights.put(texture, bitmap.getHeight());
                needsUpdating = true;
            }
        }
        
        return region;
    }

    private void rebuildAtlas() {
        var newWidth = atlasBuilder.getWidth() * 2;
        var newHeight = atlasBuilder.getHeight() * 2;

        var newAtlasBuilder = new BitmapAtlasBuilder(newWidth, newHeight);
        var newTexCoords = new HashMap<String, BitmapRegion>(texCoords.size());

        for (var entry : texCoords.entrySet()) {
            var texture = entry.getKey();
            var region = entry.getValue();
            var bitmap = region.getBitmap();

            var newRegion = newAtlasBuilder.addBitmap(bitmap);
            newTexCoords.put(texture, newRegion);
        }

        atlasBuilder = newAtlasBuilder;
        inverseWidth = 1.0f / newWidth;
        inverseHeight = 1.0f / newHeight;
        texCoords = newTexCoords;
    }
}