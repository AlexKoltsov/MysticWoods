package com.kolts.mystic.woods.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.physics.box2d.Body
import ktx.ashley.optionalPropertyFor

var Entity.physicComponent by optionalPropertyFor<PhysicComponent>()

class PhysicComponent : Component {
    lateinit var body: Body
}