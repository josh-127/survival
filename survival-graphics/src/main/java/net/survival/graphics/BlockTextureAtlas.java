package net.survival.graphics;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.stream.Collectors;

import net.survival.blocktype.BlockFace;
import net.survival.blocktype.BlockType;
import net.survival.graphics.opengl.GLFilterMode;
import net.survival.graphics.opengl.GLTexture;
import net.survival.graphics.opengl.GLWrapMode;

// TODO: Remove hard-coding.
// TODO: Make non-blocking.
public class BlockTextureAtlas implements GraphicsResource
{
    private static final int MAX_WIDTH = 256;
    private static final int MAX_HEIGHT = 256;

    private static final int TOTAL_BLOCK_FACES = BlockFace.getCachedValues().length;

    public final GLTexture blockTextures;
    private final float[] texCoords;

    public BlockTextureAtlas() {
        var builder = new BitmapAtlasBuilder(MAX_WIDTH, MAX_HEIGHT);
        var bitmapPathMap = new HashMap<String, Bitmap>();
        var regionMap = new HashMap<String, BitmapRegion>();

        for (var block : BlockType.getAllBlocks()) {
            if (block == null) {
                continue;
            }

            for (var face : BlockFace.getCachedValues()) {
                var path = block.getTexture(face);

                if (path != null) {
                    var fullPath = Paths.get(GraphicsSettings.BLOCKS_PATH, path);
                    var bitmap = Bitmap.fromFile(fullPath.toString());
                    bitmapPathMap.putIfAbsent(path, bitmap);
                }
            }
        }

        var texturePaths = bitmapPathMap.entrySet().stream()
                /*
                .sorted((o1, o2) -> {
                    var v1 = o1.getValue();
                    var v2 = o2.getValue();
                    var area1 = v1.getWidth() * v1.getHeight();
                    var area2 = v2.getWidth() * v2.getHeight();
                    if (area1 < area2) {
                        return -1;
                    }
                    if (area1 > area2) {
                        return 1;
                    }
                    return 0;
                })
                */
                .collect(Collectors.toList());

        for (var entry : texturePaths) {
            var path = entry.getKey();
            var bitmap = entry.getValue();
            var region = builder.addBitmap(bitmap);
            regionMap.putIfAbsent(path, region);
        }

        var atlasBitmap = builder.build();

        blockTextures = new GLTexture();
        blockTextures.beginBind()
                .setMinFilter(GLFilterMode.NEAREST_MIPMAP_NEAREST)
                .setMagFilter(GLFilterMode.NEAREST)
                .setWrapS(GLWrapMode.REPEAT)
                .setWrapT(GLWrapMode.REPEAT)
                .setMipmapEnabled(true)
                .setMinLod(0)
                .setMaxLod(4)
                .setData(atlasBitmap)
                .endBind();

        var totalBlocks = BlockType.getAllBlocks().length;
        var totalFaces = BlockFace.getCachedValues().length;
        texCoords = new float[totalBlocks * totalFaces * 4];

        for (var i = 0; i < BlockType.getAllBlocks().length; ++i) {
            var block = BlockType.getAllBlocks()[i];

            if (block == null) {
                continue;
            }

            for (var face : BlockFace.getCachedValues()) {
                var texturePath = block.getTexture(face);

                if (texturePath != null) {
                    var region = regionMap.get(texturePath);
                    var u1 = (float) region.getLeft() / MAX_WIDTH;
                    var v1 = 1.0f - (float) region.getTop() / MAX_HEIGHT;
                    var u2 = (float) (region.getRight() - 1) / MAX_WIDTH;
                    var v2 = 1.0f - (float) (region.getBottom() - 1) / MAX_HEIGHT;

                    texCoords[indexOfU1(i, face)] = u1;
                    texCoords[indexOfV1(i, face)] = v1;
                    texCoords[indexOfU2(i, face)] = u2;
                    texCoords[indexOfV2(i, face)] = v2;
                }
            }
        }
    }

    @Override
    public void close() {
        blockTextures.close();
    }

    public float getTexCoordU1(int blockTypeId, BlockFace blockFace) {
        return texCoords[indexOfU1(blockTypeId, blockFace)];
    }

    public float getTexCoordV1(int blockTypeId, BlockFace blockFace) {
        return texCoords[indexOfV1(blockTypeId, blockFace)];
    }

    public float getTexCoordU2(int blockTypeId, BlockFace blockFace) {
        return texCoords[indexOfU2(blockTypeId, blockFace)];
    }

    public float getTexCoordV2(int blockTypeId, BlockFace blockFace) {
        return texCoords[indexOfV2(blockTypeId, blockFace)];
    }

    private int indexOfU1(int blockTypeId, BlockFace blockFace) {
        return (blockTypeId * TOTAL_BLOCK_FACES * 4) + (blockFace.ordinal() * 4);
    }

    private int indexOfV1(int blockTypeId, BlockFace blockFace) {
        return indexOfU1(blockTypeId, blockFace) + 1;
    }

    private int indexOfU2(int blockTypeId, BlockFace blockFace) {
        return indexOfU1(blockTypeId, blockFace) + 2;
    }

    private int indexOfV2(int blockTypeId, BlockFace blockFace) {
        return indexOfU1(blockTypeId, blockFace) + 3;
    }
}