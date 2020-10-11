package net.survival.graphics

import net.survival.graphics.opengl.GLFilterMode
import net.survival.graphics.opengl.GLTexture
import net.survival.graphics.opengl.GLWrapMode
import java.nio.file.Paths
import java.util.*

private const val INITIAL_WIDTH = 64
private const val INITIAL_HEIGHT = 64

class TextureAtlas(
    private val rootPath: String,
    mipmappingEnabled: Boolean
) {
    val textureObject: GLTexture = GLTexture()
    private var needsUpdating: Boolean
    private var atlasBuilder: BitmapAtlasBuilder
    private var inverseWidth: Float
    private var inverseHeight: Float
    private var texCoords: HashMap<String, BitmapRegion>
    private val textureWidths: HashMap<String, Int>
    private val textureHeights: HashMap<String, Int>

    init {
        if (mipmappingEnabled) {
            textureObject.beginBind()
                .setMinFilter(GLFilterMode.NEAREST_MIPMAP_NEAREST)
                .setMagFilter(GLFilterMode.NEAREST)
                .setWrapS(GLWrapMode.REPEAT)
                .setWrapT(GLWrapMode.REPEAT)
                .setMipmapEnabled(true)
                .setMinLod(0)
                .setMaxLod(4)
                .setData(Bitmap(INITIAL_WIDTH, INITIAL_HEIGHT))
                .endBind()
        }
        else {
            textureObject.beginBind()
                .setMinFilter(GLFilterMode.NEAREST)
                .setMagFilter(GLFilterMode.NEAREST)
                .setWrapS(GLWrapMode.REPEAT)
                .setWrapT(GLWrapMode.REPEAT)
                .setData(Bitmap(INITIAL_WIDTH, INITIAL_HEIGHT))
                .endBind()
        }

        needsUpdating = false

        atlasBuilder = BitmapAtlasBuilder(INITIAL_WIDTH, INITIAL_HEIGHT)
        inverseWidth = 1.0f / atlasBuilder.width
        inverseHeight = 1.0f / atlasBuilder.height

        texCoords = HashMap()
        textureWidths = HashMap()
        textureHeights = HashMap()
    }

    fun close() {
        textureObject.close()
    }

    fun updateTextures() {
        if (needsUpdating) {
            textureObject.beginBind()
                .setData(atlasBuilder.build())
                .endBind()
            needsUpdating = false
        }
    }

    fun getWidth(texture: String): Int {
        lazyLoadTexture(texture)
        return textureWidths[texture]!!
    }

    fun getHeight(texture: String): Int {
        lazyLoadTexture(texture)
        return textureHeights[texture]!!
    }

    fun getTexCoordU1(texture: String): Float =
        lazyLoadTexture(texture).left.toFloat() * inverseWidth

    fun getTexCoordV1(texture: String): Float =
        1.0f - lazyLoadTexture(texture).top * inverseHeight

    fun getTexCoordU2(texture: String): Float =
        lazyLoadTexture(texture).right.toFloat() * inverseWidth

    fun getTexCoordV2(texture: String): Float =
        1.0f - lazyLoadTexture(texture).bottom * inverseHeight

    private fun lazyLoadTexture(texture: String): BitmapRegion {
        var region = texCoords[texture]

        while (region == null) {
            val fullPath = Paths.get(rootPath, texture).toString()
            val bitmap = Bitmap.fromFile(fullPath)
            region = atlasBuilder.addBitmap(bitmap)

            if (region == null) {
                rebuildAtlas()
            }
            else {
                texCoords[texture] = region
                textureWidths[texture] = bitmap.width
                textureHeights[texture] = bitmap.height
                needsUpdating = true
            }
        }

        return region
    }

    private fun rebuildAtlas() {
        val newWidth = atlasBuilder.width * 2
        val newHeight = atlasBuilder.height * 2

        val newAtlasBuilder = BitmapAtlasBuilder(newWidth, newHeight)
        val newTexCoords = HashMap<String, BitmapRegion>(texCoords.size)

        for ((texture, region) in texCoords) {
            val bitmap = region.bitmap
            val newRegion = newAtlasBuilder.addBitmap(bitmap!!)
            newTexCoords[texture] = newRegion!!
        }

        atlasBuilder = newAtlasBuilder
        inverseWidth = 1.0f / newWidth
        inverseHeight = 1.0f / newHeight
        texCoords = newTexCoords
    }
}