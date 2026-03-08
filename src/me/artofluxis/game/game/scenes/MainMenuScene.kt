package me.artofluxis.game.game.scenes

import korlibs.korge.input.*
import korlibs.korge.scene.*
import korlibs.korge.ui.*
import korlibs.korge.view.*
import korlibs.math.geom.*
import me.artofluxis.game.*
import me.artofluxis.game.registries.*
import kotlin.math.*

class MainMenuScene: Scene() {
    override suspend fun SContainer.sceneMain() {
        val background = BitmapLoader.getBitmap(GlobalRegistry.playBackgroundAsset!!)
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

            if (currentlyLoadedJARMod != saveData.selectedJARMod) {
                uiButton("Restart to update JAR Mod", Size(256.0, 96.0))
            } else {
                uiButton("Play", Size(256.0, 96.0)) {
                    onClickSuspend {
                        loadingScene()
                    }
                }
            }

            uiButton("Settings", Size(256.0, 96.0)) {
                onClickSuspend {
                    originalSceneContainer.changeTo { SettingsScene() }
                }
            }

            uiButton("Quit", Size(256.0, 96.0)) {
                onClick { views.gameWindow.close() }
            }
        }
    }
}

