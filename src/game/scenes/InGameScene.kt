package game.scenes

import game.objects.*
import game.objects.logic.*
import getBitmap
import korlibs.image.color.*
import korlibs.korge.scene.Scene
import korlibs.korge.view.*
import korlibs.math.geom.vector.*
import korlibs.time.*
import lawn.LawnType
import registries.*

class InGameScene(
    val lawnType: LawnType
) : Scene() {
    lateinit var hitboxLayer: Graphics
    lateinit var sContainer: SContainer
    val lawnObjects = mutableListOf<LawnObject>()
    val pendingObjects = mutableListOf<LawnObject>()
    val pendingToDeleteObjects = mutableListOf<LawnObject>()

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

        val plantType = PlantRegistry.get("peashooter")
        val peashooter = LawnPlant(
            lawnType.getTileCenter(1, 2), 2,
            HitboxRegistry.get("commonPlantHitHitbox"),
            TeamRegistry.get("plants"), this@InGameScene,
            null, hashSetOf(), plantType.toughness, plantType,
            mutableListOf()
        )
        plantType.traits.forEach { peashooter.traits.add(it.createInstance(peashooter)) }
        this@InGameScene.putPlant(peashooter)

        val zombieType = ZombieRegistry.get("modern-basic")
        this@InGameScene.putZombie(
            LawnZombie(
            lawnType.getTileCenter(9, 2), 2,
            HitboxRegistry.get("commonZombieHitHitbox"),
            TeamRegistry.get("zombies"), this@InGameScene,
            null, hashSetOf(), zombieType.toughness, zombieType,
            mutableListOf()
        )
        )
        this@InGameScene.putZombie(
            LawnZombie(
                lawnType.getTileCenter(8, 2), 2,
                HitboxRegistry.get("commonZombieHitHitbox"),
                TeamRegistry.get("zombies"), this@InGameScene,
                null, hashSetOf(), zombieType.toughness, zombieType,
                mutableListOf(EffectRegistry.get("chill"))
            )
        )

        addUpdater { deltaTime ->
            val dt = deltaTime.seconds

            if (pendingObjects.isNotEmpty()) {
                lawnObjects.addAll(pendingObjects)
                pendingObjects.clear()
            }

            if (pendingToDeleteObjects.isNotEmpty()) {
                lawnObjects.removeAll(pendingToDeleteObjects)
                pendingToDeleteObjects.clear()
            }

            lawnObjects.forEach { obj ->
                if (obj is TickableLawnObject) obj.tick(dt)
            }

            if (GlobalRegistry.showDebugHitboxes!!) {
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

    fun putPlant(
        obj: LawnPlant
    ) = sContainer.putObject(obj, 0.5, 0.5, lawnType.plantSize)

    fun putZombie(
        obj: LawnZombie
    ) = sContainer.putObject(obj, 0.5, 1.0, lawnType.zombieSize)

    fun putProjectile(
        obj: LawnProjectile
    ) = sContainer.putObject(obj, 0.5, 0.5, lawnType.plantSize)

    private fun SContainer.putObject(
        obj: LawnObject,
        anchorX: Double,
        anchorY: Double,
        scale: Float,
    ) {
        val position = obj.pos
        pendingObjects.add(obj)

        obj.image = image(getBitmap(obj.asset())) {
            anchor(anchorX, anchorY)
            scale(scale)
            position(position.x, position.y + lawnType.tileSize.second * when (obj) {
                is LawnPlant -> -0.25
                is LawnZombie -> 0.25
                else -> 0.0
            })
            smoothing = false
            zIndex = 100.0
        }
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
