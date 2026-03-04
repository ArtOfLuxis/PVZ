package scenes

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

class InGameScene(
    val lawnType: LawnType
) : Scene() {
    @OptIn(KorgeInternal::class)
    override suspend fun SContainer.sceneMain() {
        val lawnImage = image(lawnType.asset.readBitmap()) {
            anchor(0, 0)
            scale(0.5)
            position(0, 0)
            zIndex = -99999.0
        }
    }
}
