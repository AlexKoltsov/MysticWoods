package com.kolts.mystic.woods.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2
import ktx.ashley.optionalPropertyFor
import ktx.math.vec2

data class SpawnConfiguration(
    val model: AnimationModel,
)

var Entity.spawnComponent by optionalPropertyFor<SpawnComponent>()

class SpawnComponent(
    var type: String = "",
    var location: Vector2 = vec2(),
) : Component