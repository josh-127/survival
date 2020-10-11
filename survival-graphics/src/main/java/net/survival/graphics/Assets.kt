package net.survival.graphics

object Assets {
    private var mipmappedTextureAtlas: TextureAtlas? = null
    private var textureAtlas: TextureAtlas? = null
    private var initialized = false

    fun setup(mipmappedTextureAtlas: TextureAtlas?, textureAtlas: TextureAtlas?) {
        check(!initialized) { "Assets have already been initialized." }
        Assets.mipmappedTextureAtlas = mipmappedTextureAtlas
        Assets.textureAtlas = textureAtlas
        initialized = true
    }

    fun tearDown() {
        check(initialized) { "Assets have already been torn down." }
        mipmappedTextureAtlas!!.close()
        textureAtlas!!.close()
        initialized = false
    }

    fun getMipmappedTextureAtlas(): TextureAtlas {
        checkNotNull(mipmappedTextureAtlas) { "mipmappedTextureAtlas has not been initialized." }
        return mipmappedTextureAtlas!!
    }

    fun getTextureAtlas(): TextureAtlas {
        checkNotNull(textureAtlas) { "textureAtlas has not been initialized." }
        return textureAtlas!!
    }
}