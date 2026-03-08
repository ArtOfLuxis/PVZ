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
import me.artofluxis.game.game.scenes.MainMenuScene
import me.artofluxis.game.mod.*
import me.artofluxis.game.registries.*
import me.artofluxis.game.trait.*
import java.io.*

lateinit var runDir: File
lateinit var saveData: SaveData
val dataFolder get() = File(runDir, "mods/${saveData.selectedDataMod}/data")
val resourcesFolder get() = File(runDir, "mods/${saveData.selectedResourceMod}/resources")

var currentlyLoadedJARMod: String? = null

lateinit var originalSceneContainer: SceneContainer

suspend fun loadingScene() {
    originalSceneContainer.changeTo { LoadingScene {
        originalSceneContainer.changeTo { InGameScene(LawnRegistry.get("modern")) }

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
        println("Loaded Traits:")
        println(Trait.registry.keys.joinToString("\n\t", "\t") { it })
    }}
}

suspend fun main() {
    println("Starting...")

    //runDir = File(::main::class.java.protectionDomain.codeSource.location.toURI()).parentFile
    runDir = File("E:/Desktop/PVZGame")
    val saveFile = File(runDir, "data/save.json")

    if (!saveFile.exists()) {
        println("Saving default save file...")
        withContext(Dispatchers.IO) {
            saveFile.createNewFile()
            saveFile.writeText(resourcesVfs["default_save.json"].readString())
        }
    }

    saveData = Json.decodeFromString<SaveData>(saveFile.readText())

    val modsFolder = localVfs("$runDir/mods")
    val baseGameFolder = modsFolder["base_game"]

    if (!baseGameFolder.exists() || true) { // --------------------------
        println("Copying default base game mod folder...")
        baseGameFolder.mkdirs()
        resourcesVfs["base_game"].copyToRecursively(baseGameFolder)
    }

    if (!File(runDir, "mods/${saveData.selectedJARMod}").exists())
        saveData.selectedJARMod = "base_game"
    if (!File(runDir, "mods/${saveData.selectedDataMod}").exists())
        saveData.selectedDataMod = "base_game"
    if (!File(runDir, "mods/${saveData.selectedResourceMod}").exists())
        saveData.selectedResourceMod = "base_game"

    GlobalRegistry.load()

    Korge(
        windowSize = Size(GlobalRegistry.windowSize!!.first, GlobalRegistry.windowSize!!.second),
        title = "Plants VS Zombies: Something Something Very Cool Game",
        backgroundColor = Colors["#283743"],
        icon = GlobalRegistry.iconAsset
    ) {
        originalSceneContainer = sceneContainer()
        try {
            ModLoader.loadAndRunMod(File(runDir, "mods/${saveData.selectedJARMod}/mod.jar"))
            currentlyLoadedJARMod = saveData.selectedJARMod
        } catch (e: Exception) {
            handleException(
                e, "Encountered Exception while loading the JAR mod (${saveData.selectedJARMod})"
            )
        }
        try {
            originalSceneContainer.changeTo { MainMenuScene() }
        } catch (e: Exception) {
            handleException(e, "Encountered Exception in main scene")
        }

        Runtime.getRuntime().addShutdownHook(Thread {
            try {
                println("Closing...")
                saveData.save()
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        })
    }
}
