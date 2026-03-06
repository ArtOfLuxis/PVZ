package me.artofluxis.game.game.scenes

import me.artofluxis.game.getBitmap
import korlibs.korge.input.*
import korlibs.korge.scene.*
import korlibs.korge.ui.*
import korlibs.korge.view.*
import me.artofluxis.game.registries.*
import kotlin.math.*

class PlayScene(
    private val sContainer: SceneContainer,
    val onPlay: suspend () -> Unit
) : Scene() {
    override suspend fun SContainer.sceneMain() {
        val background = getBitmap(GlobalRegistry.playBackgroundAsset!!)
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

        uiVerticalStack(padding = 16.0) {
            position(50.0, views.virtualHeight / 2)

            uiButton("Play") { onClickSuspend { onPlay() } }
            uiButton("Settings") { onClickSuspend {
                sContainer.changeTo { SettingsScene {
                    sContainer.changeTo { PlayScene(sContainer, onPlay) }
                } }
            } }

            uiButton("Quit") { onClick { views.gameWindow.close() } }
        }
    }
}

