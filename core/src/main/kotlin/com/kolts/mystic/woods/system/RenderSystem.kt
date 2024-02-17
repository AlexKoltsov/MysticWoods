package com.kolts.mystic.woods.system

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.SortedIteratingSystem
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.scenes.scene2d.Stage
import com.kolts.mystic.woods.component.ImageComponent
import com.kolts.mystic.woods.component.imageComponent
import ktx.actors.minusAssign
import ktx.actors.plusAssign
import ktx.ashley.allOf
import ktx.assets.DisposableContainer
import ktx.assets.DisposableRegistry

class RenderSystem(
    private val stage: Stage,
) : SortedIteratingSystem(
    allOf(ImageComponent::class).get(),
    compareBy<Entity> { it.imageComponent }
), DisposableRegistry by DisposableContainer() {

    private val mapRenderer = OrthogonalTiledMapRenderer(null, 1 / 16f, stage.batch).alsoRegister()

    override fun addedToEngine(engine: Engine) {
        super.addedToEngine(engine)
        mapRenderer.map = TmxMapLoader().load("map/map.tmx")
    }

    override fun removedFromEngine(engine: Engine) {
        super.removedFromEngine(engine)
        dispose()
    }

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
}