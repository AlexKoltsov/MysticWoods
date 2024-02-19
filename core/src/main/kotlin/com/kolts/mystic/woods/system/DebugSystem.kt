package com.kolts.mystic.woods.system

import com.badlogic.ashley.systems.IntervalSystem
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Stage
import ktx.assets.DisposableRegistry
import ktx.inject.Context

class DebugSystem(
    context: Context,
    private val world: World = context.inject(),
    private val stage: Stage = context.inject(),
    private val disposableRegistry: DisposableRegistry = context.inject(),
) : IntervalSystem(PhysicsSystem.FPS) {

    private val debugRenderer = Box2DDebugRenderer().also { disposableRegistry.register(it) }

    override fun updateInterval() {
        debugRenderer.render(world, stage.camera.combined)
    }
}