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
import me.artofluxis.game.game.hitbox.*
import me.artofluxis.game.game.objects.*
import me.artofluxis.game.game.objects.logic.*
import me.artofluxis.game.game.types.*
import me.artofluxis.game.registries.*
import me.artofluxis.game.trait.events.alive.*

class InGameScene(
    val lawnType: LawnType
) : Scene() {
    private lateinit var hitboxLayer: Graphics
    private lateinit var sContainer: SContainer
    val lawnObjects = mutableListOf<LawnObject>()
    private val pendingObjects = mutableListOf<LawnObject>()
    private val pendingToDeleteObjects = mutableListOf<LawnObject>()

    var paused = false
    var pauseOverlay: Container? = null

    override suspend fun SContainer.sceneMain() {
        hitboxLayer = graphics {}
        sContainer = this

        image(BitmapLoader.getBitmap(lawnType.asset)) {
            anchor(0.0, 0.0)
            scale(1.0)
            smoothing = false
            zIndex = -99999.0
        }

        keys.down {
            if (it.key == Key.ESCAPE) {
                if (paused)
                    pauseOverlay!!.removeFromParent()
                else
                    pauseOverlay = sContainer.showPauseMenu()

                paused = !paused
            }
        }

        this@InGameScene.putPlantByType(
            1, 2,
            PlantRegistry.get("peashooter"),
            TeamRegistry.get("plants")
        )
        this@InGameScene.putPlantByType(
            1, 4,
            PlantRegistry.get("peashooter"),
            TeamRegistry.get("plants")
        ).applyEffect(EffectRegistry.get("chill"), Double.POSITIVE_INFINITY)


        this@InGameScene.putZombieByType(
            9, 2,
            ZombieRegistry.get("modern_basic"),
            TeamRegistry.get("zombies")
        ).applyEffect(EffectRegistry.get("chill"), Double.POSITIVE_INFINITY)
        this@InGameScene.putZombieByType(
            10, 2,
            ZombieRegistry.get("modern_basic"),
            TeamRegistry.get("zombies")
        ).applyEffect(EffectRegistry.get("chill"), Double.POSITIVE_INFINITY)
        this@InGameScene.putZombieByType(
            11, 2,
            ZombieRegistry.get("modern_basic"),
            TeamRegistry.get("zombies")
        ).applyEffect(EffectRegistry.get("chill"), Double.POSITIVE_INFINITY)

        this@InGameScene.putZombieByType(
            9, 4,
            ZombieRegistry.get("modern_basic"),
            TeamRegistry.get("zombies")
        ).applyEffect(EffectRegistry.get("chill"), Double.POSITIVE_INFINITY)
        this@InGameScene.putZombieByType(
            10, 4,
            ZombieRegistry.get("modern_basic"),
            TeamRegistry.get("zombies")
        ).applyEffect(EffectRegistry.get("chill"), Double.POSITIVE_INFINITY)
        this@InGameScene.putZombieByType(
            11, 4,
            ZombieRegistry.get("modern_basic"),
            TeamRegistry.get("zombies")
        ).applyEffect(EffectRegistry.get("chill"), Double.POSITIVE_INFINITY)

        val fpsText = text("FPS: 0") {
            position(4, 4)
            textSize = 16.0
            color = Colors.WHITE
            zIndex = 10000000.0
        }

        val frameTimes = ArrayDeque<Double>()
        val averagingWindow = 2.0

        var hitboxTimer = 0.0
        val debugHitboxes = ArrayList<Pair<Hitbox, LawnObject>>(256)
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

                if (pendingObjects.isNotEmpty()) {
                    lawnObjects.addAll(pendingObjects)
                    pendingObjects.clear()
                }

                if (pendingToDeleteObjects.isNotEmpty()) {
                    lawnObjects.removeAll { it in pendingToDeleteObjects }
                    pendingToDeleteObjects.clear()
                }

                lawnObjects.forEach { obj ->
                    if (obj is TickableLawnObject) obj.tick(dt)
                }

                if (GlobalRegistry.showDebugHitboxes == true) {
                    hitboxTimer += dt
                    if (hitboxTimer >= GlobalRegistry.debugHitboxUpdateInterval!!) {
                        hitboxTimer = 0.0

                        debugHitboxes.clear()

                        for (obj in lawnObjects) {
                            if (obj is DamageableLawnObject) {
                                debugHitboxes.add(obj.hitHitbox to obj)
                            }

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
                        e, "Encountered Exception in on-lawn scene"
                    )
                }
            }
        }
    }

    fun putPlant(obj: LawnPlant) =
        sContainer.putObject(obj, 0.5, 0.5)
    fun putZombie(obj: LawnZombie) =
        sContainer.putObject(obj, 0.5, 1.0)
    fun putProjectile(obj: LawnProjectile) =
        sContainer.putObject(obj, 0.5, 0.5)

    fun putPlantByType(column: Int, row: Int, plantType: PlantType, team: ObjectTeam): LawnPlant {
        val plant = LawnPlant(
            lawnType.getTileCenter(column, row),
            row,
            plantType.hitHitbox,
            team,
            this,
            null,
            hashSetOf(),
            hashMapOf(),
            plantType
        )
        plantType.traits.forEach { plant.addTrait(it.createInstance(plant)) }
        putPlant(plant)

        return plant
    }

    fun putZombieByType(column: Int, row: Int, zombieType: ZombieType, team: ObjectTeam): LawnZombie {
        val zombie = LawnZombie(
            lawnType.getTileCenter(column, row),
            row,
            zombieType.hitHitbox,
            team,
            this,
            null,
            hashSetOf(),
            hashMapOf(),
            zombieType
        )
        zombieType.traits.forEach { zombie.addTrait(it.createInstance(zombie)) }
        putZombie(zombie)

        return zombie
    }

    fun putProjectileByType(position: Position, row: Int, projectileType: ProjectileType, team: ObjectTeam, shooter: LawnObject): LawnProjectile {
        val projectile = LawnProjectile(
            position,
            row,
            team,
            this,
            null,
            hashSetOf(),
            projectileType,
            shooter
        )
        projectileType.traits.forEach { projectile.addTrait(it.createInstance(projectile)) }
        putProjectile(projectile)

        return projectile
    }

    private fun SContainer.putObject(obj: LawnObject, anchorX: Double, anchorY: Double) {
        val position = obj.pos
        pendingObjects.add(obj)

        val asset = obj.asset()
        if (asset != null) obj.setNewImage(image(BitmapLoader.getBitmap(asset)) {
            anchor(anchorX, anchorY)
            scale(obj.scale())
            position(
                position.x + lawnType.tileSize.first  * obj.offset().first  * obj.scale(),
                position.y + lawnType.tileSize.second * obj.offset().second * obj.scale()
            )
            smoothing = false
            zIndex = 100.0
        })

        if (obj is ObjectCreatedTraitListener) obj.onCreation()
    }

    fun removeObject(obj: LawnObject) {
        require(obj in lawnObjects) {
            "Tried to delete a lawn object that doesn't exist or is still pending"
        }

        if (obj is TickableLawnObject) {
            obj.destroyTraits()
        }

        obj.image?.removeFromParent()
        obj.image = null


        pendingToDeleteObjects.add(obj)
    }

    fun SContainer.showPauseMenu() = container {
        xy(views.virtualWidth / 2, views.virtualHeight / 2) // center
        scale(1.0)
        zIndex = 999999.0

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
