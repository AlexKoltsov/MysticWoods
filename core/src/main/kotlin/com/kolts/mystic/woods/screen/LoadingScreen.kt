package com.kolts.mystic.woods.screen

import com.badlogic.gdx.assets.AssetManager
import com.kolts.mystic.woods.MysticWoodsGame
import com.kolts.mystic.woods.TextureAtlasAsset
import ktx.app.KtxScreen
import ktx.inject.Context
import ktx.log.logger

class LoadingScreen(
    context: Context,
    private val assetManager: AssetManager = context.inject(),
    private val game: MysticWoodsGame = context.inject(),
) : KtxScreen {

    override fun show() {
        log.debug { "LoadingScreen gets shown" }
        loadAssets()
        changeScreen()
    }

    private fun loadAssets() {
        assetManager.apply {
            TextureAtlasAsset.entries.forEach { load(it.assetDescriptor) }
            finishLoading()
        }
    }

    private fun changeScreen() {
        game.run {
            addScreen(GameScreen(context))
            setScreen<GameScreen>()
            removeScreen<LoadingScreen>()
        }
    }

    companion object {
        private val log = logger<GameScreen>()
    }
}