package me.artofluxis.game.game.hitbox

import korlibs.image.color.*
import me.artofluxis.game.Position
import me.artofluxis.game.game.types.LawnType
import korlibs.math.geom.Rectangle
import me.artofluxis.game.game.objects.*
import me.artofluxis.game.game.scenes.*

class Hitbox(
    val id: String,
    val width: Double,       // relative to tileSize
    val height: Double,      // relative to tileSize
    val xOffset: Double,     // relative to tileSize
    val yOffset: Double,     // relative to tileSize
    val hitboxCenter: HitboxCenter,
    val affectOtherRows: Boolean,
    val debugColor: RGBA
) {
    fun bounds(pos: Position, lawnType: LawnType, scale: Double): Rectangle {
        val tileW = lawnType.tileSize.first.toDouble()
        val tileH = lawnType.tileSize.second.toDouble()

        val w = width * tileW * scale
        val h = height * tileH * scale
        val ox = xOffset * tileW * scale
        val oy = yOffset * tileH * scale

        val ax = hitboxCenter.ordinal % 3 * 0.5
        val ay = hitboxCenter.ordinal / 3 * 0.5

        val left = pos.x + ox - w * ax
        val top = pos.y + oy - h * ay

        return Rectangle(left, top, w, h)
    }

    // AABB intersection with a point
    fun contains(
        hitboxPosition: Position,
        point: Position,
        lawnType: LawnType,
        scale: Double
    ): Boolean {
        val r = bounds(hitboxPosition, lawnType, scale)
        return point.x in r.left..r.right && point.y in r.top..r.bottom
    }

    // AABB intersection with another hitbox (allow different scales)
    fun contains(
        other: Hitbox,
        hitboxPosition: Position,
        otherHitboxPos: Position,
        lawnType: LawnType,
        scale: Double,
        otherScale: Double = 1.0
    ): Boolean {
        val r1 = this.bounds(hitboxPosition, lawnType, scale)
        val r2 = other.bounds(otherHitboxPos, lawnType, otherScale)

        return r1.left < r2.right &&
            r1.right > r2.left &&
            r1.top < r2.bottom &&
            r1.bottom > r2.top
    }

    override fun toString(): String = "Hitbox[$id]"

    fun isIntersecting(
        obj: LawnObject,
        otherObj: LawnObject,
        otherHitbox: Hitbox,
        lawnType: LawnType,
        condition: (LawnObject, Hitbox) -> Boolean,
    ): Boolean {
        return otherObj != obj &&
            condition(otherObj, otherHitbox) &&
            this.contains(
                otherHitbox,
                obj.pos,
                otherObj.pos,
                lawnType,
                obj.scale(),
                otherObj.scale()
            ) &&
            (
                this.affectOtherRows ||
                    otherHitbox.affectOtherRows ||
                    (obj.row == otherObj.row)
            )
    }

    fun isIntersecting(
        obj: LawnObject,
        otherObj: LawnObject,
        lawnType: LawnType,
        condition: (LawnObject, Hitbox) -> Boolean,
    ): Boolean {
        return isIntersecting(
            obj, otherObj,
            otherObj.hitHitbox() ?: return false,
            lawnType, condition
        )
    }

    fun findIntersectingObjects(obj: LawnObject) = findIntersectingObjects(obj) { _, _ -> true }

    fun findIntersectingObjects(
        obj: LawnObject,
        filter: (LawnObject, Hitbox) -> Boolean,
    ): List<LawnObject> {
        val scene = obj.scene
        return scene.lawnObjects.filter { other ->
            isIntersecting(obj, other, scene.lawnType, filter)
        }
    }

    fun anyIntersectingObject(
        obj: LawnObject,
        condition: (LawnObject, Hitbox) -> Boolean,
    ): Boolean {
        val scene = obj.scene
        return scene.lawnObjects.any { other ->
            isIntersecting(obj, other, scene.lawnType, condition)
        }
    }
}
