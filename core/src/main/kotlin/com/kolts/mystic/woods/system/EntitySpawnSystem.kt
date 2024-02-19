package com.kolts.mystic.woods.system

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.kolts.mystic.woods.MysticWoodsGame.Companion.UNIT_SCALE
import com.kolts.mystic.woods.TextureAtlasAsset
import com.kolts.mystic.woods.component.*
import com.kolts.mystic.woods.event.MapChangedEvent
import com.kolts.mystic.woods.toBox2d
import ktx.app.gdxError
import ktx.ashley.add
import ktx.ashley.allOf
import ktx.ashley.entity
import ktx.ashley.with
import ktx.box2d.body
import ktx.box2d.box
import ktx.inject.Context
import ktx.log.logger
import ktx.math.times
import ktx.math.vec2
import ktx.tiled.property

class EntitySpawnSystem(
    context: Context,
    private val world: World = context.inject(),
    private val engine: Engine = context.inject(),
    private val assetManager: AssetManager = context.inject(),
) :
    IteratingSystem(allOf(SpawnComponent::class).get()), EventListener {

    private val spawnConfigurationCache = mutableMapOf<SpawnType, SpawnConfiguration>()
    private val sizeCache = mutableMapOf<AnimationModel, Vector2>()

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val spawnComponent = entity.spawnComponent!!
        val spawnCfg = getSpawnConfiguration(spawnComponent)

        engine.add {
            removeEntity(entity)

            entity {
                with<ImageComponent> {}
                with<AnimationComponent> {
                    model = spawnCfg.model
                }
                with<PhysicComponent> {
                    body = world.body {
                        userData = spawnCfg.size
                        fixedRotation = true
                        position.x = spawnCfg.position.x.toBox2d(spawnCfg.size.x)
                        position.y = spawnCfg.position.y.toBox2d(spawnCfg.size.y)
                        box(
                            width = spawnCfg.size.x,
                            height = spawnCfg.size.y
                        ) {}
                    }
                }
            }
        }
    }

    private fun getSpawnConfiguration(spawnComponent: SpawnComponent) = with(spawnComponent) {
        spawnConfigurationCache.getOrPut(type) {
            when (type) {
                SpawnType.PLAYER -> SpawnConfiguration(
                    model = AnimationModel.PLAYER_IDLE,
                    position = location,
                    size = getSize(AnimationModel.PLAYER_IDLE)
                )

                SpawnType.SLIME -> SpawnConfiguration(
                    model = AnimationModel.SLIME_IDLE,
                    position = location,
                    size = getSize(AnimationModel.SLIME_IDLE)
                )
            }
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
                                location = vec2(mapObject.x, mapObject.y) * UNIT_SCALE
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