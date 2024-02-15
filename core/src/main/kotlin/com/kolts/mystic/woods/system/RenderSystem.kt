package com.kolts.mystic.woods.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.SortedIteratingSystem
import com.badlogic.gdx.scenes.scene2d.Stage
import com.kolts.mystic.woods.component.ImageComponent
import com.kolts.mystic.woods.component.imageComponent
import ktx.actors.minusAssign
import ktx.actors.plusAssign
import ktx.ashley.allOf

class RenderSystem(
    private val stage: Stage,
) : SortedIteratingSystem(
    allOf(ImageComponent::class).get(),
    compareBy<Entity> { it.imageComponent }
) {

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
            act(deltaTime)
            draw()
        }

        entity.imageComponent!!.image.toFront()
    }


}