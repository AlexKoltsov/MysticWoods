package com.kolts.mystic.woods

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration

fun main() {
    val game = Game()
    val config = Lwjgl3ApplicationConfiguration()
        .apply {
            setTitle("MysticWoods")
            setWindowedMode(1280, 720)
            useVsync(true)
            setForegroundFPS(60)
        }

    Lwjgl3Application(game, config)
}
