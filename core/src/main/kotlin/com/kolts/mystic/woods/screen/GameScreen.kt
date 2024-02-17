package com.kolts.mystic.woods.screen

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.kolts.mystic.woods.event.MapChangedEvent
import com.kolts.mystic.woods.event.fire
import com.kolts.mystic.woods.system.AnimationSystem
import com.kolts.mystic.woods.system.EntitySpawnSystem
import com.kolts.mystic.woods.system.RenderSystem
import ktx.app.KtxScreen
import ktx.inject.Context
import ktx.log.logger

class GameScreen(
    private val context: Context,
    private val engine: Engine = context.inject(),
    private val stage: Stage = context.inject(),
) : KtxScreen {

    override fun show() {
        log.debug { "GameScreen gets shown" }

        engine.apply {
            addSystem(AnimationSystem(context))
            addSystem(RenderSystem(context))
            addSystem(EntitySpawnSystem(context))

            systems
                .filterIsInstance<EventListener>()
                .forEach { context.inject<Stage>().addListener(it) }
        }

        MapChangedEvent(TmxMapLoader().load("map/map.tmx"))
            .let { stage.fire(it) }
    }

    override fun render(delta: Float) {
        engine.update(delta)
    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
    }

    companion object {
        private val log = logger<GameScreen>()
    }
}