package com.kolts.mystic.woods.screen

import com.kolts.mystic.woods.Game
import com.kolts.mystic.woods.TextureAtlasAsset
import ktx.app.KtxScreen
import ktx.log.logger

class LoadingScreen(private val game: Game) : KtxScreen {

    override fun show() {
        log.debug { "LoadingScreen gets shown" }
        loadAssets()
        changeScreen()
    }

    private fun loadAssets() {
        TextureAtlasAsset.entries.forEach { game.assetManager.load(it.assetDescriptor) }
        game.assetManager.finishLoading()
    }

    private fun changeScreen() {
        game.addScreen(GameScreen(game))
        game.setScreen<GameScreen>()
        game.removeScreen<LoadingScreen>()
    }

    companion object {
        private val log = logger<GameScreen>()
    }
}