package com.kolts.mystic.woods.event

import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.Stage

fun Stage.fire(event: Event) = root.fire(event)

data class MapChangedEvent(val map: TiledMap) : Event()