package com.kolts.mystic.woods.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.SortedIteratingSystem
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.kolts.mystic.woods.MysticWoodsGame.Companion.UNIT_SCALE
import com.kolts.mystic.woods.component.ImageComponent
import com.kolts.mystic.woods.component.imageComponent
import com.kolts.mystic.woods.event.MapChangedEvent
import ktx.actors.minusAssign
import ktx.actors.plusAssign
import ktx.ashley.allOf
import ktx.assets.DisposableRegistry
import ktx.inject.Context
import ktx.log.logger

class RenderSystem(
    context: Context,
    private val stage: Stage = context.inject(),
    private val disposableRegistry: DisposableRegistry = context.inject(),
) : SortedIteratingSystem(allOf(ImageComponent::class).get(), compareBy<Entity> { it.imageComponent }), EventListener {

    private val mapRenderer = OrthogonalTiledMapRenderer(null, UNIT_SCALE, stage.batch)
        .also { disposableRegistry.register(it) }

    override fun entityAdded(entity: Entity) {
        super.entityAdded(entity)
        stage += entity.imageComponent!!.image
    }

    override fun entityRemoved(entity: Entity) {
        super.entityRemoved(entity)
        stage -= entity.imageComponent!!.image
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        with(stage) {
            viewport.apply()

            mapRenderer.setView(stage.camera as OrthographicCamera)
            mapRenderer.render()

            act(deltaTime)
            draw()
        }

        entity.imageComponent!!.image.toFront()
    }

    override fun handle(event: Event): Boolean {
        return when (event) {
            is MapChangedEvent -> {
                mapRenderer.map = event.map
                true
            }

            else -> false
        }
    }

    companion object {
        private val log = logger<RenderSystem>()
    }
}