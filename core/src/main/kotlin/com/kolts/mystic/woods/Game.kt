package com.kolts.mystic.woods

import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.kolts.mystic.woods.screen.LoadingScreen
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.assets.DisposableContainer
import ktx.assets.DisposableRegistry
import ktx.assets.disposeSafely

class Game(
    private val disposableRegistry: DisposableRegistry = DisposableContainer(),
) : KtxGame<KtxScreen>(), DisposableRegistry by disposableRegistry {

    val assetManager: AssetManager = AssetManager().alsoRegister()

    override fun create() {
        Gdx.app.logLevel = Application.LOG_DEBUG
        addScreen(LoadingScreen(this))
        setScreen<LoadingScreen>()
    }

    override fun dispose() {
        disposableRegistry.disposeSafely()
    }

    companion object {
        const val UNIT_SCALE = 1 / 16f
    }
}