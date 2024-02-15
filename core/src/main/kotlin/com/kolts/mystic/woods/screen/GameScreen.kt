package com.kolts.mystic.woods.screen

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.utils.viewport.ExtendViewport
import ktx.actors.plusAssign
import ktx.actors.setPosition
import ktx.actors.stage
import ktx.app.KtxScreen
import ktx.assets.DisposableContainer
import ktx.assets.DisposableRegistry
import ktx.assets.disposeSafely
import ktx.log.logger

class GameScreen(
    private val disposableRegistry: DisposableRegistry = DisposableContainer(),
) : KtxScreen, DisposableRegistry by disposableRegistry {

    private val stage: Stage = stage(viewport = ExtendViewport(16f, 9f)).alsoRegister()
    private val texture: Texture = Texture("assets/graphic/player.png").alsoRegister()

    override fun show() {
        log.debug { "GameScreen gets shown" }
        stage += Image(texture).apply {
            setPosition(1, 1)
            setSize(1f, 1f)
        }
    }

    override fun render(delta: Float) {
        with(stage) {
            act(delta)
            draw()
        }
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