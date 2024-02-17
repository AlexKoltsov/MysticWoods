package com.kolts.mystic.woods

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.graphics.g2d.TextureAtlas

enum class TextureAtlasAsset {
    MYSTIC_WOODS
    ;

    private val path: String = "graphic/${name.lowercase()}.atlas"
    val assetDescriptor: AssetDescriptor<TextureAtlas> = AssetDescriptor(path, TextureAtlas::class.java)
}
