package com.kolts.mystic.woods.system

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.kolts.mystic.woods.MysticWoodsGame.Companion.UNIT_SCALE
import com.kolts.mystic.woods.TextureAtlasAsset
import com.kolts.mystic.woods.component.*
import com.kolts.mystic.woods.event.MapChangedEvent
import ktx.app.gdxError
import ktx.ashley.add
import ktx.ashley.allOf
import ktx.ashley.entity
import ktx.ashley.with
import ktx.inject.Context
import ktx.log.logger
import ktx.math.times
import ktx.math.vec2
import ktx.tiled.property

class EntitySpawnSystem(
    context: Context,
    private val engine: Engine = context.inject(),
    private val assetManager: AssetManager = context.inject(),
) :
    IteratingSystem(allOf(SpawnComponent::class).get()), EventListener {

    private val spawnConfigurationCache = mutableMapOf<SpawnType, SpawnConfiguration>()
    private val sizeCache = mutableMapOf<AnimationModel, Vector2>()

    override fun processEntity(entity: Entity, deltaTime: Float) {
        entity.spawnComponent?.let {
            val spawnConfiguration = getSpawnConfiguration(it.type)
            val model = spawnConfiguration.model

            engine.add {
                removeEntity(entity)

                entity {
                    with<ImageComponent> {
                        image = Image().apply {
                            getSize(model).let { size -> setSize(size.x, size.y) }
                            setPosition(it.location.x, it.location.y)
                        }
                    }
                    with<AnimationComponent> {
                        this.model = model
                    }
                }
            }
        }
    }

    private fun getSpawnConfiguration(type: SpawnType) = spawnConfigurationCache.getOrPut(type) {
        when (type) {
            SpawnType.PLAYER -> SpawnConfiguration(AnimationModel.PLAYER_IDLE)
            SpawnType.SLIME -> SpawnConfiguration(AnimationModel.SLIME_IDLE)
        }
    }

    private fun getSize(model: AnimationModel) = sizeCache.getOrPut(model) {
        val atlasKey = model.atlasKey
        val regions = assetManager[TextureAtlasAsset.MYSTIC_WOODS.assetDescriptor].findRegions(atlasKey)
        if (regions.isEmpty) {
            gdxError("There are no texture regions for $atlasKey")
        }
        vec2(regions.first().originalHeight.toFloat(), regions.first().originalWidth.toFloat()) * UNIT_SCALE
    }

    override fun handle(event: Event): Boolean {
        return when (event) {
            is MapChangedEvent -> {
                val entitiesLayer = event.map.layers["entities"] ?: gdxError("Entities layer not found")
                entitiesLayer
                    .objects
                    .filterIsInstance<TiledMapTileMapObject>()
                    .forEach { mapObject ->
                        engine.entity {
                            with<SpawnComponent> {
                                type = spawnType(mapObject)
                                location.x = mapObject.x * UNIT_SCALE
                                location.y = mapObject.y * UNIT_SCALE
                            }
                        }
                    }

                true
            }

            else -> false
        }
    }

    private fun spawnType(mapObject: TiledMapTileMapObject): SpawnType = mapObject.tile.property<String>("type")
        .let { SpawnType.valueOf(it.uppercase()) }

    companion object {
        private val log = logger<EntitySpawnSystem>()
    }
}