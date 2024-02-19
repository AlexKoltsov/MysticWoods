package com.kolts.mystic.woods.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IntervalIteratingSystem
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.World
import com.kolts.mystic.woods.component.ImageComponent
import com.kolts.mystic.woods.component.PhysicComponent
import com.kolts.mystic.woods.component.imageComponent
import com.kolts.mystic.woods.component.physicComponent
import com.kolts.mystic.woods.toLibgdx
import ktx.ashley.allOf
import ktx.inject.Context
import ktx.math.component1
import ktx.math.component2

class PhysicsSystem(
    context: Context,
    private val world: World = context.inject(),
) : IntervalIteratingSystem(allOf(PhysicComponent::class, ImageComponent::class).get(), FPS) {

    override fun updateInterval() {
        super.updateInterval()
        world.step(1f, 6, 2)
    }

    override fun processEntity(entity: Entity) {
        val physicComponent = entity.physicComponent!!
        val imageComponent = entity.imageComponent!!

        val (bodyX, bodyY) = physicComponent.body.position
        val (width, height) = physicComponent.body.userData as Vector2
        imageComponent.image.apply {
            setPosition(bodyX.toLibgdx(width), bodyY.toLibgdx(height))
            setSize(width, height)
        }
    }

    companion object {
        const val FPS = 1 / 60f;
    }
}