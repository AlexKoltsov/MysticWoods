package com.kolts.mystic.woods.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2
import ktx.ashley.optionalPropertyFor

enum class SpawnType {
    PLAYER,
    SLIME,
}

data class SpawnConfiguration(
    val model: AnimationModel,
    val position: Vector2,
    val size: Vector2,
)

var Entity.spawnComponent by optionalPropertyFor<SpawnComponent>()

class SpawnComponent : Component {
    lateinit var location: Vector2
    lateinit var type: SpawnType
}