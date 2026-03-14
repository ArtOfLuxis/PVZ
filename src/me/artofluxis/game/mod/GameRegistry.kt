package me.artofluxis.game.mod

import me.artofluxis.game.game.objects.*
import me.artofluxis.game.game.scenes.*

object GameRegistry {
    lateinit var gameLoop: (deltaTime: Double, scene: InGameScene) -> Unit

    fun registerGameLoop(gameLoop: (deltaTime: Double, scene: InGameScene) -> Unit) {
        this.gameLoop = gameLoop
    }

    lateinit var putObjectHandler: (
            scene: InGameScene,
            obj: LocationalLawnObject,
            anchorX: Double,
            anchorY: Double,
            scale: Double
        ) -> Unit

    fun registerPutObject(putObjectHandler: (
        scene: InGameScene,
        obj: LocationalLawnObject,
        anchorX: Double,
        anchorY: Double,
        scale: Double
    ) -> Unit) {
        this.putObjectHandler = putObjectHandler
    }

    lateinit var removeObjectHandler: (
        scene: InGameScene,
        obj: LocationalLawnObject
    ) -> Unit

    fun registerRemoveObject(removeObjectHandler: (
        scene: InGameScene,
        obj: LocationalLawnObject
    ) -> Unit) {
        this.removeObjectHandler = removeObjectHandler
    }
}
