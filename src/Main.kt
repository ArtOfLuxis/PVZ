import game.scenes.*
import korlibs.image.color.*
import korlibs.korge.*
import korlibs.korge.scene.*
import korlibs.math.geom.*
import mod.*
import registries.*

suspend fun main() {
    println("Starting...")
    GlobalRegistry.load()

    loadAndRunMod("E:\\IDE\\projects\\PVZBaseGameMod\\build\\libs\\PVZBaseGameMod-1.0.jar")

    Korge(
        windowSize = Size(1020, 540),
        title = "Plants VS Zombies: Something Something Very Cool Game",
        backgroundColor = Colors["#283743"],
        icon = GlobalRegistry.iconAsset
    ) {
        try {
            val sceneContainer = sceneContainer()
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
            }}
        } catch (e: Exception) {
            handleException(e, this)
        }
    }
}
