package net.survival.client.graphics;

import net.survival.blocktype.BlockFace;
import net.survival.blocktype.BlockType;
import net.survival.client.graphics.opengl.GLFilterMode;
import net.survival.client.graphics.opengl.GLTexture;
import net.survival.client.graphics.opengl.GLWrapMode;

// TODO: Remove hard-coding
// TODO: Make non-blocking
public class BlockTextureAtlas implements GraphicsResource
{
    public final GLTexture blockTextures;
    public final BlockFace blockFace;

    private final float[] texCoords;

    public BlockTextureAtlas(BlockFace blockFace) {
        var atlas = new Bitmap(256, 256);
        for (var block : BlockType.getAllBlocks()) {
            if (block == null)
                continue;
            if (block.getTexture(blockFace) == null)
                continue;

            var dstIndex = block.getTypeId();
            var dstX = (dstIndex % 16) * 16;
            var dstY = (dstIndex / 16) * 16;
            var blockBitmap = Bitmap.fromFile(
                    GraphicsSettings.BLOCKS_PATH +
                    block.getTexture(blockFace));
            Bitmap.blit(blockBitmap, 0, 0, 16, 16, atlas, dstX, dstY);
        }

        blockTextures = new GLTexture();
        blockTextures.beginBind()
                .setMinFilter(GLFilterMode.NEAREST_MIPMAP_NEAREST)
                .setMagFilter(GLFilterMode.NEAREST)
                .setWrapS(GLWrapMode.REPEAT)
                .setWrapT(GLWrapMode.REPEAT)
                .setMipmapEnabled(true)
                .setMinLod(0)
                .setMaxLod(4)
                .setData(atlas)
                .endBind();

        this.blockFace = blockFace;

        texCoords = new float[17 * 17 * 4];
        for (var i = 0; i < 256; ++i) {
            var indexU1 = i * 4;
            var indexV1 = indexU1 + 1;
            var indexU2 = indexU1 + 2;
            var indexV2 = indexU1 + 3;

            var tileU = (i % 16);
            var tileV = 15 - (i / 16);

            texCoords[indexU1] = tileU / 16.0f;
            texCoords[indexV1] = (tileV / 16.0f) + (1.0f / 16.0f);
            texCoords[indexU2] = (tileU / 16.0f) + (1.0f / 16.0f);
            texCoords[indexV2] = tileV / 16.0f;
        }
    }

    @Override
    public void close() {
        blockTextures.close();
    }

    public float getTexCoordU1(int blockId) {
        return texCoords[blockId << 2];
    }

    public float getTexCoordV1(int blockId) {
        return texCoords[(blockId << 2) + 1];
    }

    public float getTexCoordU2(int blockId) {
        return texCoords[(blockId << 2) + 2];
    }

    public float getTexCoordV2(int blockId) {
        return texCoords[(blockId << 2) + 3];
    }
}