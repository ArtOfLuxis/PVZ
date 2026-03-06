package me.artofluxis.game.game.scenes

import me.artofluxis.game.getBitmap
import korlibs.korge.input.*
import korlibs.korge.scene.*
import korlibs.korge.ui.*
import korlibs.korge.view.*
import me.artofluxis.game.registries.*
import kotlin.math.*

class SettingsScene(
    val onBack: suspend () -> Unit = {}
) : Scene() {
    override suspend fun SContainer.sceneMain() {
        val background = getBitmap(GlobalRegistry.settingsBackgroundAsset!!)
        image(background).apply {
            smoothing = false
            anchor(.5, .5)
            position(views.virtualWidth / 2, views.virtualHeight / 2)

            val scale = min(
                views.virtualWidth / background.width,
                views.virtualHeight / background.height
            )
            scale(scale)
        }

        uiButton("Back") {
            position(views.virtualWidth - 120.0, 20.0)
            onClickSuspend {
                onBack()
            }
        }
    }
}
