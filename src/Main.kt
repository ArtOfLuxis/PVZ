import korlibs.image.color.*
import korlibs.io.file.std.*
import korlibs.korge.*
import korlibs.korge.scene.*
import korlibs.math.geom.*
import kotlinx.serialization.json.*
import lawn.*
import plant.traits.*
import registries.*
import game.scenes.InGameScene

suspend fun main() {
    GlobalRegistry.load()

    Korge(
        windowSize = Size(1020, 540),
        backgroundColor = Colors["#000000"],
        icon = GlobalRegistry.iconAsset
    ) {
        SpriteRegistry.load()
        HitboxRegistry.load()
        TeamRegistry.load()
        EffectRegistry.load()
        ProjectileRegistry.load()
        PlantRegistry.load()
        SunDropperRegistry.load()
        TileRegistry.load()
        LawnRegistry.load()

        val sceneContainer = sceneContainer()

        sceneContainer.changeTo { InGameScene(LawnRegistry.get("modern")) }

        println(
            """
                Sprites: ${SpriteRegistry.sprites}
                Effects: ${EffectRegistry.effectTypes}
                Hitboxes: ${HitboxRegistry.hitboxes}
                Teams: ${TeamRegistry.teams}
                Projectiles: ${ProjectileRegistry.projectiles}
                Plants: ${PlantRegistry.plants}
                Sun Droppers: ${SunDropperRegistry.sunDroppers}
                Tiles: ${TileRegistry.tiles}
                Lawns: ${LawnRegistry.lawns}
            """.trimIndent()
        )
    }
}
