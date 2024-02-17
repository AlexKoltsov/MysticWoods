package com.kolts.mystic.woods.screen

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.kolts.mystic.woods.Game
import com.kolts.mystic.woods.event.MapChangedEvent
import com.kolts.mystic.woods.event.fire
import com.kolts.mystic.woods.system.AnimationSystem
import com.kolts.mystic.woods.system.EntitySpawnSystem
import com.kolts.mystic.woods.system.RenderSystem
import ktx.actors.stage
import ktx.app.KtxScreen
import ktx.assets.DisposableContainer
import ktx.assets.DisposableRegistry
import ktx.assets.disposeSafely
import ktx.log.logger

class GameScreen(
    private val game: Game,
    private val disposableRegistry: DisposableRegistry = DisposableContainer(),
) : KtxScreen, DisposableRegistry by disposableRegistry {

    private val stage: Stage = stage(viewport = ExtendViewport(16f, 9f)).alsoRegister()

    private val engine: Engine = Engine()
        .apply {
            addSystem(AnimationSystem(game.assetManager))
            addSystem(RenderSystem(stage))
            addSystem(EntitySpawnSystem(this, game.assetManager))

            systems
                .filterIsInstance<EventListener>()
                .forEach { stage.addListener(it) }
        }

    override fun show() {
        log.debug { "GameScreen gets shown" }

        MapChangedEvent(TmxMapLoader().load("map/map.tmx"))
            .let { stage.fire(it) }
    }

    override fun render(delta: Float) {
        engine.update(delta);
    }

    override fun dispose() {
        disposableRegistry.disposeSafely()
        engine.removeAllSystems()
    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
    }

    companion object {
        private val log = logger<GameScreen>()
    }
}