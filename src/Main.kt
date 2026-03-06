import game.scenes.*
import korlibs.image.color.*
import korlibs.korge.*
import korlibs.korge.scene.*
import korlibs.math.geom.*
import plant.traits.*
import registries.*
import projectile.traits.*
import tile.traits.*
import trait.*
import zombie.traits.*

suspend fun main() {
    GlobalRegistry.load()
    // plant traits
    Trait.register("StraightShooter", ::StraightShooterTrait)

    // projectile traits
    Trait.register("CommonProjectileLogic", ::ProjectileLogicTrait)
    Trait.register("EffectApplier", ::EffectApplierTrait)
    Trait.register("FlammableProjectile", ::FlammableProjectileTrait)

    // zombie traits
    Trait.register("CommonZombieLogic", ::CommonZombieLogicTrait)

    // tile traits
    Trait.register("EffectApplierTile", ::EffectApplierTileTrait)

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
            this.gameWindow.setSize(1200, 600)
            this.gameWindow.title = "Encountered Exception"

            val sceneContainer = sceneContainer()
            sceneContainer.changeTo { ExceptionScene(e) }
        }
    }
}
