package me.artofluxis.game.game.objects

import korlibs.image.bitmap.*
import korlibs.korge.view.*
import korlibs.korge.view.filter.*
import korlibs.math.geom.slice.*
import me.artofluxis.game.*
import me.artofluxis.game.animation.*
import me.artofluxis.game.game.hitbox.*
import me.artofluxis.game.game.scenes.*

interface LawnObject {
    var pos: Position
    val row: Int
    var team: ObjectTeam?
    val scene: InGameScene
    var image: Image?
    val animationPlayer: AnimationPlayer?
    val highlightFilter: HighlightFilter

    fun setNewImage(newImage: Image) {
        this.image = newImage
        this.image!!.filter = highlightFilter
    }

    fun updateImage(newBitmap: RectSlice<Bitmap>) {
        this.image!!.apply {
            bitmap = newBitmap
        }
    }

    fun scale() = scene.lawnType.teamSizes[team]
        ?: error("Unknown scale for team $team on lawn ${scene.lawnType}")

    fun offset() = scene.lawnType.teamOffsets[team]
        ?: error("Unknown offset for team $team on lawn ${scene.lawnType}")


    fun hitHitbox(): Hitbox?

    fun findIntersectingObjects(filter: (LawnObject, Hitbox) -> Boolean): List<LawnObject> {
        return (this.hitHitbox() ?: error(
            "Tried to find intersecting objects from an object without a hitHitbox"
        )).findIntersectingObjects(this, filter)
    }

    fun isIntersecting(
        hitbox: Hitbox,
        otherObj: LawnObject,
        condition: (LawnObject, Hitbox) -> Boolean
    ): Boolean {
        return (this.hitHitbox() ?: error(
            "Tried to find intersecting objects from an object without a hitHitbox"
        )).isIntersecting(this, otherObj, this.scene.lawnType, condition)
    }

    fun isIntersecting(
        hitbox: Hitbox,
        otherObj: LawnObject,
    ) = this.isIntersecting(hitbox, otherObj) { _, _ -> true}
}
