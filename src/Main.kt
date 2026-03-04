import korlibs.image.color.*
import korlibs.io.file.std.*
import korlibs.korge.*
import korlibs.korge.scene.*
import korlibs.math.geom.*
import kotlinx.serialization.json.*
import lawn.*
import plant.traits.*
import registries.*
import scenes.InGameScene

suspend fun main() = Korge(windowSize = Size(1020, 540), backgroundColor = Colors["#000000"]) {
    ProjectileRegistry.load()
    PlantRegistry.load()
    SunDropperRegistry.load()
    TileRegistry.load()
    LawnRegistry.load()

	val sceneContainer = sceneContainer()

	//sceneContainer.changeTo { InGameScene(lawnType) }


    println("""
        Projectiles: ${ProjectileRegistry.projectiles}
        Plants: ${PlantRegistry.plants}
        Sun Droppers: ${SunDropperRegistry.sunDroppers}
        Tiles: ${TileRegistry.tiles}
        Lawns: ${LawnRegistry.lawns}
    """.trimIndent())
}
