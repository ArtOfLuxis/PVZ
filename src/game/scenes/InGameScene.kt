package game.scenes

import Position
import game.*
import korlibs.image.format.readBitmap
import korlibs.korge.input.*
import korlibs.korge.internal.*
import korlibs.korge.scene.Scene
import korlibs.korge.view.SContainer
import korlibs.korge.view.anchor
import korlibs.korge.view.image
import korlibs.korge.view.position
import korlibs.korge.view.scale
import lawn.LawnType
import registries.*

class InGameScene(
    val lawnType: LawnType
) : Scene() {
    lateinit var sContainer: SContainer
    val onLawnObjects = hashMapOf<Position, MutableList<OnLawnObject>>()

    @OptIn(KorgeInternal::class)
    override suspend fun SContainer.sceneMain() {
        sContainer = this

        val plantType = PlantRegistry.get("peashooter")
        sContainer.putObject(OnLawnPlant(
            lawnType.getTileCenter(1, 1),
            HitboxRegistry.get("commonHitHitbox"),
            TeamRegistry.get("plants"),
            plantType, plantType.toughness,
            listOf()
        ))
        sContainer.putObject(OnLawnPlant(
            lawnType.getTileCenter(2, 1),
            HitboxRegistry.get("commonHitHitbox"),
            TeamRegistry.get("plants"),
            plantType, plantType.toughness,
            listOf()
        ))
        sContainer.putObject(OnLawnPlant(
            lawnType.getTileCenter(1, 2),
            HitboxRegistry.get("commonHitHitbox"),
            TeamRegistry.get("plants"),
            plantType, plantType.toughness,
            listOf()
        ))
        sContainer.putObject(OnLawnPlant(
            lawnType.getTileCenter(2, 2),
            HitboxRegistry.get("commonHitHitbox"),
            TeamRegistry.get("plants"),
            plantType, plantType.toughness,
            listOf()
        ))

        onClick { println(it.currentPosGlobal) }

        image(lawnType.asset.readBitmap()) {
            anchor(0, 0)
            scale(0.5)
            position(0, 0)
            smoothing = false
            zIndex = -99999.0
        }
    }

    suspend fun SContainer.putObject(obj: OnLawnObject) {
        val position = obj.pos

        if (position !in onLawnObjects) onLawnObjects[position] = mutableListOf()
        onLawnObjects[position]!!.add(obj)

        image(obj.asset().readBitmap()) {
            anchor(0.5, 0.5)
            scale(lawnType.plantSize)
            position(obj.pos.x, obj.pos.y)
            smoothing = false
            zIndex = onLawnObjects[position]!!.size - 1.0
        }
    }
}
