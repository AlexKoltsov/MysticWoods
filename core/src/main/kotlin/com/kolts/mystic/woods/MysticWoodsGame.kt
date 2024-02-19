package com.kolts.mystic.woods

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.kolts.mystic.woods.screen.LoadingScreen
import ktx.actors.stage
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.assets.DisposableContainer
import ktx.assets.DisposableRegistry
import ktx.assets.disposeSafely
import ktx.box2d.createWorld
import ktx.inject.Context
import ktx.inject.register

class MysticWoodsGame(
    private val disposableRegistry: DisposableRegistry = DisposableContainer(),
) : KtxGame<KtxScreen>(), DisposableRegistry by disposableRegistry {

    val context: Context = Context().alsoRegister()

    override fun create() {
        Gdx.app.logLevel = Application.LOG_DEBUG

        context.register {
            bindSingleton<DisposableRegistry> { disposableRegistry }
            bindSingleton<MysticWoodsGame> { this@MysticWoodsGame }
            bindSingleton<AssetManager> { AssetManager() }
            bindSingleton<Stage> { stage(viewport = ExtendViewport(16f, 9f)) }
            bindSingleton<Engine> { Engine() }
            bindSingleton<World> { createWorld() }
        }

        addScreen(LoadingScreen(context))
        setScreen<LoadingScreen>()
    }

    override fun dispose() {
        super.dispose()
        context.remove<DisposableRegistry>()
        context.remove<MysticWoodsGame>()
        disposableRegistry.disposeSafely()
    }

    companion object {
        const val UNIT_SCALE = 1 / 16f
    }
}