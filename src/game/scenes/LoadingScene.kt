package game.scenes

import korlibs.image.color.*
import korlibs.korge.scene.*
import korlibs.korge.view.*
import registries.*

class LoadingScene(
    val onLoad: suspend () -> Unit
): Scene() {
    override suspend fun SContainer.sceneMain() {
        try {
            val padding = 50.0
            val barWidth = 800.0
            val barHeight = 50.0

            val loadingText = text("", textSize = 48.0, color = Colors.WHITE) {
                xy(padding, padding)
            }

            val progressBarBg = solidRect(barWidth, barHeight, Colors.DARKGREY) {
                xy(padding, padding + 60)
            }

            val progressBar = solidRect(0.0, barHeight, Colors.GREEN) {
                xy(padding, padding + 60)
            }

            // list of registries in order of loading
            val registriesToLoad = listOf(
                SpriteRegistry,
                HitboxRegistry,
                TeamRegistry,
                EffectRegistry,
                ProjectileRegistry,
                PlantRegistry,
                ZombieRegistry,
                SunDropperRegistry,
                TileRegistry,
                LawnRegistry
            )

            for ((index, registry) in registriesToLoad.withIndex()) {
                loadingText.text = "Loading ${registry::class.simpleName}..."
                registry.load()
                val progress = (index + 1).toDouble() / registriesToLoad.size
                progressBar.width = barWidth * progress
            }

            onLoad()
        } catch (e: Exception) {
            this.removeChildren()

            this.stage!!.gameWindow.setSize(1200, 600)
            this.stage!!.gameWindow.title = "Encountered Exception"

            val sceneContainer = sceneContainer()
            sceneContainer.changeTo { ExceptionScene(e) }
        }
    }
}
