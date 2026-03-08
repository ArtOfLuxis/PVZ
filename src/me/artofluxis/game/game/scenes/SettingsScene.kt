package me.artofluxis.game.game.scenes

import korlibs.image.color.*
import korlibs.korge.input.*
import korlibs.korge.scene.*
import korlibs.korge.ui.*
import korlibs.korge.view.*
import korlibs.math.geom.*
import kotlinx.coroutines.*
import kotlinx.serialization.json.*
import me.artofluxis.game.*
import me.artofluxis.game.registries.*
import java.io.*
import kotlin.math.*

class SettingsScene : Scene() {
    enum class ModType(val visualName: String, val saveGetter: () -> String) {
        JAR("JAR Mod", { saveData.selectedJARMod }),
        DATA("Data Mod", { saveData.selectedDataMod }),
        RESOURCES("Resource Mod", { saveData.selectedResourceMod })
    }
    override suspend fun SContainer.sceneMain() {
        val background = BitmapLoader.getBitmap(GlobalRegistry.settingsBackgroundAsset!!)
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

        uiButton("Back", Size(64.0, 64.0)) {
            position(views.virtualWidth - 84.0, 20.0)
            onClickSuspend {
                originalSceneContainer.changeTo { MainMenuScene() }
            }
        }

        val mods = File(runDir, "mods").listFiles { dir, name ->
            File(dir, name).let { it.isDirectory && File(it, "mod.json").exists() }
        }!!

        val options = listOf(
            ModType.JAR to mods.filter { File(it, "mod.jar").exists() },
            ModType.DATA to mods.filter { File(it, "data").exists() },
            ModType.RESOURCES to mods.filter { File(it, "resources").exists() }
        )

        uiHorizontalStack(padding = 50.0) {
            position(150, 200)

            for ((modType, values) in options) {
                container {
                    uiText(modType.visualName, Size(100, 100)) {
                        scale = 1.5
                        xy(-10, -30)
                        zIndex = 2.0
                    }

                    solidRect(
                        Size(200, 300),
                        Colors.BLACK.withAd(0.9)
                    ) {
                        anchor(0.2, 0.5)
                        zIndex = 1.0
                    }

                    val items = values.map {
                        val json = Json.parseToJsonElement(File(it, "mod.json").readText())
                        json.jsonObject["name"]!!.jsonPrimitive.content
                    }

                    uiHorizontalStack(padding = 10.0) {
                        zIndex = 3.0
                        val selector = uiComboBox(
                            Size(100, 50),
                            items = items,
                            selectedIndex = values.indexOfFirst { it.name == modType.saveGetter() },
                        )

                        selector.onSelectionUpdate {
                            val fileName = values[selector.selectedIndex].name
                            when (modType) {
                                ModType.JAR -> saveData.selectedJARMod = fileName
                                ModType.DATA -> saveData.selectedDataMod = fileName
                                ModType.RESOURCES -> {
                                    BitmapLoader.bitmapCache.clear()
                                    saveData.selectedResourceMod = fileName
                                    this.stage!!.launch { GlobalRegistry.load() }
                                }
                            }
                            saveData.save()
                        }
                    }
                }
            }
        }
    }
}
