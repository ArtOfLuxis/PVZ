package me.artofluxis.game

import korlibs.image.color.*
import korlibs.io.file.std.*
import korlibs.korge.*
import korlibs.korge.scene.*
import korlibs.math.geom.*
import kotlinx.coroutines.*
import kotlinx.serialization.json.*
import me.artofluxis.game.game.save.SaveData
import me.artofluxis.game.game.scenes.InGameScene
import me.artofluxis.game.game.scenes.LoadingScene
import me.artofluxis.game.game.scenes.PlayScene
import me.artofluxis.game.mod.loadAndRunMod
import me.artofluxis.game.registries.*
import java.io.*

lateinit var runDir: File
lateinit var saveData: SaveData
val dataFolder get() = File(runDir, "mods/${saveData.selectedDataMod}/data")
val resourcesFolder get() = File(runDir, "mods/${saveData.selectedResourceMod}/resources")

suspend fun main() {
    println("Starting...")

    //runDir = File(::main::class.java.protectionDomain.codeSource.location.toURI()).parentFile
    runDir = File("E:\\Desktop\\PVZGame")
    val saveFile = File(runDir, "data\\save.json")

    if (!saveFile.exists()) {
        withContext(Dispatchers.IO) {
            saveFile.createNewFile()
            saveFile.writeText(resourcesVfs["default_save.json"].readString())
        }
    }

    saveData = Json.decodeFromString<SaveData>(saveFile.readText())

    loadAndRunMod(File(runDir, "mods/${saveData.selectedJARMod}/mod.jar"))

    GlobalRegistry.load()

    Korge(
        windowSize = Size(GlobalRegistry.windowSize!!.first, GlobalRegistry.windowSize!!.second),
        title = "Plants VS Zombies: Something Something Very Cool Game",
        backgroundColor = Colors["#283743"],
        icon = GlobalRegistry.iconAsset
    ) {
        try {
            val sceneContainer = sceneContainer()
            sceneContainer.changeTo { PlayScene(sceneContainer) {
                sceneContainer.changeTo { LoadingScene {
                    sceneContainer.changeTo { InGameScene(LawnRegistry.get("modern")) }

                    println(
                        """
                    Loaded Registries:
                        Sprites: ${SpriteRegistry.sprites}
                        Effects: ${EffectRegistry.effectTypes}
                        Hitboxes: ${HitboxRegistry.hitboxes}
                        Teams: ${TeamRegistry.teams}
                        Projectiles: ${ProjectileRegistry.projectiles}
                        Plants: ${PlantRegistry.plants}
                        Zombies: ${ZombieRegistry.zombies}
                        Sun Droppers: ${SunDropperRegistry.sunDroppers}
                        Tiles: ${TileRegistry.tiles}
                        Lawns: ${LawnRegistry.lawns}
                    """.trimIndent()
                    )
            }}}
            }
        } catch (e: Exception) {
            handleException(e, this)
        }
    }
}
