package com.kolts.mystic.woods.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import ktx.ashley.optionalPropertyFor

enum class AnimationModel {
    PLAYER_IDLE,
    PLAYER_IDLE_RIGHT,
    PLAYER_IDLE_UP,
    PLAYER_RUN_RIGHT,
    PLAYER_RUN_UP,
    PLAYER_RUN_DOWN,
    PLAYER_ATTACK_RIGHT,
    PLAYER_ATTACK_UP,
    PLAYER_ATTACK_DOWN,
    PLAYER_DEATH,

    SLIME_IDLE
    ;

    val atlasKey: String = name.lowercase()
}

var Entity.animationComponent by optionalPropertyFor<AnimationComponent>()

class AnimationComponent : Component {
    var stateTime: Float = 0f
    lateinit var model: AnimationModel
}