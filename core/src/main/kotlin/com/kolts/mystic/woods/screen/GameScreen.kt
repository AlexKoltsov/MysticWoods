package com.kolts.mystic.woods.screen

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.kolts.mystic.woods.component.ImageComponent
import com.kolts.mystic.woods.system.RenderSystem
import ktx.actors.stage
import ktx.app.KtxScreen
import ktx.ashley.entity
import ktx.ashley.with
import ktx.assets.DisposableContainer
import ktx.assets.DisposableRegistry
import ktx.assets.disposeSafely
import ktx.log.logger

class GameScreen(
    private val disposableRegistry: DisposableRegistry = DisposableContainer(),
) : KtxScreen, DisposableRegistry by disposableRegistry {

    private val stage: Stage = stage(viewport = ExtendViewport(16f, 9f)).alsoRegister()

    private val engine: Engine = Engine()
        .apply {
            addSystem(RenderSystem(stage))
        }

    override fun show() {
        log.debug { "GameScreen gets shown" }

        val texture = Texture("assets/graphic/player.png").alsoRegister()

        repeat(5) {
            engine.entity {
                with<ImageComponent> {
                    image = Image(texture).apply {
                        setSize(1f, 1f)
                        setPosition(it.toFloat(), it.toFloat())
                    }
                }
            }
        }
    }

    override fun render(delta: Float) {
        engine.update(delta);
    }

    override fun dispose() {
        disposableRegistry.disposeSafely()
    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
    }

    companion object {
        private val log = logger<GameScreen>()
    }
}