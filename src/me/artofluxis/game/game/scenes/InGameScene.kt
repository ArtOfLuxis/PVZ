package me.artofluxis.game.game.scenes

import me.artofluxis.game.getBitmap
import korlibs.korge.scene.Scene
import korlibs.korge.view.*
import korlibs.math.geom.vector.*
import korlibs.time.*
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

    override suspend fun SContainer.sceneMain() {
        hitboxLayer = graphics {}
        sContainer = this

        image(getBitmap(lawnType.asset)) {
            anchor(0, 0)
            scale(0.5)
            position(0, 0)
            smoothing = false
            zIndex = -99999.0
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
        )

        this@InGameScene.putZombieByType(
            10, 2,
            ZombieRegistry.get("modern_basic"),
            TeamRegistry.get("zombies")
        )

        this@InGameScene.putZombieByType(
            11, 2,
            ZombieRegistry.get("modern_basic"),
            TeamRegistry.get("zombies")
        )

        this@InGameScene.putZombieByType(
            9, 4,
            ZombieRegistry.get("modern_basic"),
            TeamRegistry.get("zombies")
        )

        this@InGameScene.putZombieByType(
            10, 4,
            ZombieRegistry.get("modern_basic"),
            TeamRegistry.get("zombies")
        )

        this@InGameScene.putZombieByType(
            11, 4,
            ZombieRegistry.get("modern_basic"),
            TeamRegistry.get("zombies")
        )

        var hitboxTimer = 0.0
        addUpdater { deltaTime ->
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

            if (GlobalRegistry.showDebugHitboxes!!) {
                hitboxTimer += dt
                if (hitboxTimer >= GlobalRegistry.debugHitboxUpdateInterval!!) {
                    hitboxTimer = 0.0
                    hitboxLayer.updateShape {
                        clear()
                        lawnObjects.forEach { obj ->
                            val r = obj.hitHitbox.bounds(obj.pos, lawnType)
                            stroke(korlibs.image.color.Colors.RED, StrokeInfo(2.0)) {
                                rect(r.left, r.top, r.width, r.height)
                            }
                        }
                    }
                }
            }
        }
    }

    fun putPlant(
        obj: LawnPlant
    ) = sContainer.putObject(obj, 0.5, 0.5, lawnType.plantSize)

    fun putZombie(
        obj: LawnZombie
    ) = sContainer.putObject(obj, 0.5, 1.0, lawnType.zombieSize)

    fun putProjectile(
        obj: LawnProjectile
    ) = sContainer.putObject(obj, 0.5, 0.5, lawnType.plantSize)

    fun putPlantByType(column: Int, row: Int, plantType: PlantType, team: ObjectTeam): LawnPlant {
        val plant = LawnPlant(
            lawnType.getTileCenter(column, row),
            row,
            plantType.hitHitbox,
            team,
            this,
            null,
            hashSetOf(),
            plantType.toughness,
            hashMapOf(),
            plantType
        )
        plantType.traits.forEach { plant.traits.add(it.createInstance(plant)) }
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
            zombieType.toughness,
            hashMapOf(),
            zombieType
        )
        zombieType.traits.forEach { zombie.traits.add(it.createInstance(zombie)) }
        putZombie(zombie)

        return zombie
    }

    private fun SContainer.putObject(
        obj: LawnObject,
        anchorX: Double,
        anchorY: Double,
        scale: Float,
    ) {
        val position = obj.pos
        pendingObjects.add(obj)

        obj.setNewImage(image(getBitmap(obj.asset())) {
            anchor(anchorX, anchorY)
            scale(scale)
            position(position.x, position.y + lawnType.tileSize.second * when (obj) {
                is LawnPlant -> -0.25
                is LawnZombie -> 0.25
                else -> 0.0
            })
            smoothing = false
            zIndex = 100.0
        })

        if (obj is ObjectCreatedTraitListener) obj.onCreation()
    }

    fun removeObject(
        obj: LawnObject
    ) {
        require(obj in lawnObjects) {
            "Tried to delete a lawn object that doesn't exist or is still pending"
        }

        obj.image!!.removeFromParent()
        pendingToDeleteObjects.add(obj)
    }
}
