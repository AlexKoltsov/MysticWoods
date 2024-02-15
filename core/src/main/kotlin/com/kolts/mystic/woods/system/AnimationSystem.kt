package com.kolts.mystic.woods.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.kolts.mystic.woods.component.*
import ktx.app.gdxError
import ktx.ashley.allOf
import ktx.collections.map
import ktx.log.logger


class AnimationSystem(
    private val textureAtlas: TextureAtlas,
) : IteratingSystem(
    allOf(ImageComponent::class, AnimationComponent::class).get()
) {

    private val cachedAnimations = mutableMapOf<String, Animation<TextureRegionDrawable>>()

    override fun processEntity(entity: Entity?, deltaTime: Float) {
        val animationComponent = entity!!.animationComponent!!
        val imageComponent = entity.imageComponent!!

        animationComponent.stateTime += deltaTime
        imageComponent.image.drawable = animation(animationComponent.model).getKeyFrame(animationComponent.stateTime)
    }

    private fun animation(animationModel: AnimationModel): Animation<TextureRegionDrawable> {
        val atlasKey = animationModel.atlasKey
        return cachedAnimations.getOrPut(atlasKey) {
            log.debug { "New animation is created for $atlasKey" }
            val regions = textureAtlas.findRegions(atlasKey)
            if (regions.isEmpty) {
                gdxError("There are no texture regions for $atlasKey")
            }
            Animation(DEFAULT_FRAME_DURATION, regions.map { TextureRegionDrawable(it) }, Animation.PlayMode.LOOP)
        }
    }

    companion object {
        private val log = logger<AnimationSystem>()
        private const val DEFAULT_FRAME_DURATION = 1 / 8f
    }
}