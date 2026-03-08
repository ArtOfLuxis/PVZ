package me.artofluxis.game.game.scenes

import me.artofluxis.game.handleException
import korlibs.image.color.*
import korlibs.korge.scene.*
import korlibs.korge.ui.*
import korlibs.korge.view.*
import korlibs.math.geom.*
import me.artofluxis.game.registries.*

class LoadingScene(
    val onLoad: suspend () -> Unit
): Scene() {
    override suspend fun SContainer.sceneMain() {
        try {
            val padding = 50.0
            val barWidth = 1100.0
            val barHeight = 50.0

            val loadingText = text("", textSize = 48.0, color = Colors.WHITE) {
                xy(padding, padding)
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

            val progressBar = uiProgressBar(
                Size(barWidth, barHeight),
                0.0, registriesToLoad.size
            ) {
                xy(padding, padding + 60)
            }


            for ((index, registry) in registriesToLoad.withIndex()) {
                loadingText.text = "Loading ${registry::class.simpleName}..."
                registry.load()
                progressBar.current = (index + 1).toDouble()
            }

            onLoad()
        } catch (e: Exception) {
            handleException(e, "Encountered Exception while loading registries")
        }
    }
}
