package me.artofluxis.game.game.scenes

import korlibs.event.*
import korlibs.image.bitmap.*
import korlibs.image.color.*
import korlibs.korge.input.*
import korlibs.korge.scene.*
import korlibs.korge.ui.*
import korlibs.korge.view.*
import korlibs.math.geom.*
import korlibs.time.*
import kotlinx.coroutines.*
import me.artofluxis.game.*
import me.artofluxis.game.animation.*
import me.artofluxis.game.game.hitbox.*
import me.artofluxis.game.game.objects.*
import me.artofluxis.game.game.objects.logic.*
import me.artofluxis.game.game.types.*
import me.artofluxis.game.mod.*
import me.artofluxis.game.registries.*

@Suppress("MemberVisibilityCanBePrivate")
class InGameScene(
    val lawnType: LawnType
) : Scene() {
    lateinit var sContainer: SContainer
    val lawnObjects = mutableListOf<LocationalLawnObject>()
    val pendingObjects = mutableListOf<LocationalLawnObject>()
    val pendingToDeleteObjects = mutableListOf<LocationalLawnObject>()

    var paused = false
    var pauseOverlay: Container? = null

    lateinit var lawn: Lawn

    override suspend fun SContainer.sceneMain() {
        sContainer = this

        val animationPlayer = AnimationPlayer(lawnType.animationPack)

        val lawnImage = image(animationPlayer.frame()) {
            anchor(0.0, 0.0)
            scale(1.0)
            smoothing = false
            zIndex = -99999.0
        }

        lawn = Lawn(
            this@InGameScene,
            lawnImage, animationPlayer,
            hashSetOf(), lawnType
        )
        animationPlayer.parent = lawn

        keys.down {
            if (it.key == Key.ESCAPE) {
                if (paused)
                    pauseOverlay!!.removeFromParent()
                else
                    pauseOverlay = sContainer.showPauseMenu()

                paused = !paused
            }
        }

        lawnType.tileSet.forEachIndexed { row, rows ->
            rows.forEachIndexed { column, tile ->
                if (tile.animationPack != null && tile.traits.isNotEmpty())
                    putTileByType(column + 1, row + 1, tile)
            }
        }

        this@InGameScene.putPlantByType(
            1, 2,
            PlantRegistry.get("peashooter"),
            TeamRegistry.get("plants")
        )

        this@InGameScene.putPlantByType(
            1, 3,
            PlantRegistry.get("peashooter"),
            TeamRegistry.get("plants")
        )

        this@InGameScene.putPlantByType(
            1, 4,
            PlantRegistry.get("peashooter"),
            TeamRegistry.get("plants")
        )


        this@InGameScene.putZombieByType(
            9, 2,
            ZombieRegistry.get("modern_basic"),
            TeamRegistry.get("zombies")
        )

        this@InGameScene.putZombieByType(
            9, 4,
            ZombieRegistry.get("modern_basic"),
            TeamRegistry.get("zombies")
        )

        val fpsText = text("FPS: 0") {
            position(4, 4)
            textSize = 20.0
            color = Colors.WHITE
            zIndex = 10000.0
        }

        val frameTimes = ArrayDeque<Double>()
        val averagingWindow = 5.0

        var hitboxTimer = 0.0
        val debugHitboxes = ArrayList<Pair<Hitbox, LocationalLawnObject>>(256)
        var debugOverlayBitmap = Bitmap32(
            views.gameWindow.width,
            views.gameWindow.height
        )

        val debugOverlayImage = image(debugOverlayBitmap) {
            anchor(0.0, 0.0)
            smoothing = false
            zIndex = 9999999.0
        }

        addUpdater { deltaTime ->
            if (paused) return@addUpdater

            frameTimes.addLast(deltaTime.seconds)

            var totalTime = frameTimes.sum()
            while (totalTime > averagingWindow && frameTimes.isNotEmpty()) {
                totalTime -= frameTimes.removeFirst()
            }

            val avgDeltaTime = if (frameTimes.isNotEmpty()) totalTime / frameTimes.size else 1.0
            val fps = if (avgDeltaTime > 0.0) (1.0 / avgDeltaTime) else 0.0

            fpsText.text = "FPS: ${fps.toInt()}"

            try {
                val dt = deltaTime.seconds

                GameRegistry.gameLoop.invoke(dt, this@InGameScene)

                if (GlobalRegistry.showDebugHitboxes == true) {
                    hitboxTimer += dt
                    if (hitboxTimer >= GlobalRegistry.debugHitboxUpdateInterval!!) {
                        hitboxTimer = 0.0

                        debugHitboxes.clear()

                        for (obj in lawnObjects) {
                            val hitbox = obj.hitHitbox()
                            if (hitbox != null)
                                debugHitboxes.add(hitbox to obj)

                            if (obj is TickableLawnObject) {
                                for (traitInstance in obj.getTraitsSnapshot()) {
                                    val cachedSnapshot = traitInstance.trait.cachedValues.entries.toList()
                                    for (entry in cachedSnapshot) {
                                        val value = entry.value
                                        if (value is Hitbox) debugHitboxes.add(value to obj)
                                    }
                                }
                            }
                        }

                        if (debugOverlayBitmap.width != views.gameWindow.width ||
                            debugOverlayBitmap.height != views.gameWindow.height) {

                            debugOverlayBitmap = Bitmap32(
                                views.gameWindow.width,
                                views.gameWindow.height
                            )
                            debugOverlayImage.bitmap = debugOverlayBitmap.slice()
                        }

                        debugOverlayBitmap.fill(Colors.TRANSPARENT)

                        for ((hitbox, obj) in debugHitboxes) {
                            val r = hitbox.bounds(obj.pos, lawnType, obj.scale())

                            debugOverlayBitmap.drawRectOutline(
                                r.left.toInt(),
                                r.top.toInt(),
                                r.width.toInt(),
                                r.height.toInt(),
                                hitbox.debugColor
                            )
                        }

                        debugOverlayBitmap.contentVersion++
                        debugOverlayImage.invalidate()
                    }
                }
            } catch (e: Exception) {
                this@InGameScene.launch {
                    handleException(
                        e, "Encountered Exception in on-lawn scene updater"
                    )
                }
            }
        }
    }

    fun putPlant(obj: LawnPlant) =
        this.putObject(obj, 0.5, 0.5, obj.type.scale)
    fun putZombie(obj: LawnZombie) =
        this.putObject(obj, 0.5, 1.0, obj.type.scale)
    fun putProjectile(obj: LawnProjectile) =
        this.putObject(obj, 0.5, 0.5, 1.0)
    fun putTile(obj: LawnTile) =
        this.putObject(obj, 0.5, 0.5, 1.0)

    fun putPlantByType(
        column: Int,
        row: Int,
        plantType: PlantType,
        team: ObjectTeam?
    ): LawnPlant {
        val animPlayer = AnimationPlayer(plantType.animationPack)

        val plant = LawnPlant(
            lawnType.getTileCenter(column, row),
            row,
            team,
            this,
            null,
            animPlayer,
            hashSetOf(),
            hashMapOf(),
            plantType
        )

        plantType.traits.forEach { plant.addTrait(it.createInstance(plant)) }
        putPlant(plant)

        return plant
    }

    fun putZombieByType(column: Int, row: Int, zombieType: ZombieType, team: ObjectTeam?): LawnZombie {
        val animPlayer = AnimationPlayer(zombieType.animationPack)

        val zombie = LawnZombie(
            lawnType.getTileCenter(column, row),
            row,
            team,
            this,
            null,
            animPlayer,
            hashSetOf(),
            hashMapOf(),
            zombieType
        )

        zombieType.traits.forEach { zombie.addTrait(it.createInstance(zombie)) }
        putZombie(zombie)

        return zombie
    }

    fun putProjectileByType(position: Position, row: Int, projectileType: ProjectileType, team: ObjectTeam?, shooter: LocationalLawnObject): LawnProjectile {
        val animPlayer = AnimationPlayer(projectileType.animationPack)

        val projectile = LawnProjectile(
            position,
            row,
            team,
            this,
            null,
            animPlayer,
            hashSetOf(),
            projectileType,
            shooter
        )

        projectileType.traits.forEach { projectile.addTrait(it.createInstance(projectile)) }
        putProjectile(projectile)

        return projectile
    }

    fun putTileByType(column: Int, row: Int, tileType: TileType): LawnTile {
        val animPlayer =
            if (tileType.animationPack == null) null
            else AnimationPlayer(tileType.animationPack)

        val tile = LawnTile(
            lawnType.getTileCenter(column, row),
            row,
            this,
            null,
            animPlayer,
            hashSetOf(),
            tileType
        )

        tileType.traits.forEach { tile.addTrait(it.createInstance(tile)) }
        putTile(tile)

        return tile
    }

    private fun putObject(obj: LocationalLawnObject, anchorX: Double, anchorY: Double, scale: Double) {
        GameRegistry.putObjectHandler.invoke(
            this@InGameScene,
            obj, anchorX, anchorY,
            scale
        )
    }

    fun removeObject(obj: LocationalLawnObject) {
        GameRegistry.removeObjectHandler.invoke(this@InGameScene, obj)
    }

    fun SContainer.showPauseMenu() = container {
        xy(views.virtualWidth / 2, views.virtualHeight / 2) // center
        scale(1.0)
        zIndex = 9999999999.0

        // background
        solidRect(views.virtualWidth.toDouble(), views.virtualHeight.toDouble(), Colors.BLACK.withAd(0.6)) {
            anchor(0.5, 0.5)
        }

        val resume = {
            this@container.removeFromParent()
            paused = false
        }

        uiButton("Resume", Size(256.0, 96.0)) {
            position(-100.0, -60.0)
            onClick { resume() }
        }

        uiButton("Main Menu", Size(256.0, 96.0)) {
            position(-100.0, 60.0)
            onClick {
                resume()
                this@InGameScene.launch {
                    originalSceneContainer.changeTo { MainMenuScene() }
                }
            }
        }
    }
}
