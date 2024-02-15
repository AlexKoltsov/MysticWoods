package com.kolts.mystic.woods

import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.kolts.mystic.woods.screen.GameScreen
import ktx.app.KtxGame
import ktx.app.KtxScreen

class Game : KtxGame<KtxScreen>() {

    override fun create() {
        Gdx.app.logLevel = Application.LOG_DEBUG
        addScreen(GameScreen())
        setScreen<GameScreen>()
    }
}