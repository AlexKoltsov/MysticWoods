package com.kolts.mystic.woods.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2
import ktx.ashley.optionalPropertyFor
import ktx.math.vec2

enum class SpawnType {
    PLAYER
}

data class SpawnConfiguration(
    val model: AnimationModel,
)

var Entity.spawnComponent by optionalPropertyFor<SpawnComponent>()

class SpawnComponent(
    var location: Vector2 = vec2(),
) : Component {
    lateinit var type: SpawnType
}