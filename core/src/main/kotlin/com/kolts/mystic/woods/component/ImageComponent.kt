package com.kolts.mystic.woods.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.scenes.scene2d.ui.Image
import ktx.ashley.optionalPropertyFor

var Entity.imageComponent by optionalPropertyFor<ImageComponent>()

class ImageComponent : Component, Comparable<ImageComponent> {

    lateinit var image: Image

    override fun compareTo(other: ImageComponent): Int {
        return compareValuesBy(
            this,
            other,
            { it.image.x },
            { it.image.y }
        )
    }
}