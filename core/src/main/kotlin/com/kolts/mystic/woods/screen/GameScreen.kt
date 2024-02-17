package com.kolts.mystic.woods.screen

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.kolts.mystic.woods.component.AnimationComponent
import com.kolts.mystic.woods.component.AnimationModel
import com.kolts.mystic.woods.component.ImageComponent
import com.kolts.mystic.woods.system.AnimationSystem
import com.kolts.mystic.woods.system.RenderSystem
import ktx.actors.setPosition
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
    private val textureAtlas = TextureAtlas("graphic/mysticWoods.atlas").alsoRegister()

    private val engine: Engine = Engine()
        .apply {
            addSystem(AnimationSystem(textureAtlas))
            addSystem(RenderSystem(stage))
        }

    override fun show() {
        log.debug { "GameScreen gets shown" }

        AnimationModel.entries
            .mapIndexed { index, model ->
                engine.entity {
                    with<ImageComponent> {
                        image = Image().apply {
                            setSize(1f, 1f)
                            setPosition(index, 0)
                        }
                    }
                    with<AnimationComponent> {
                        this.model = model
                    }
                }
            }
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