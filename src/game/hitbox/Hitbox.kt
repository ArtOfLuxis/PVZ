package game.hitbox

import Position
import game.types.LawnType
import korlibs.math.geom.Rectangle

class Hitbox(
    val id: String,
    val width: Double,       // relative to tileSize
    val height: Double,      // relative to tileSize
    val xOffset: Double,     // relative to tileSize
    val yOffset: Double,     // relative to tileSize
    val hitboxCenter: HitboxCenter,
    val affectOtherRows: Boolean
) {
    fun bounds(pos: Position, lawnType: LawnType): Rectangle {
        val tileW = lawnType.tileSize.first.toDouble()
        val tileH = lawnType.tileSize.second.toDouble()

        val w = width * tileW
        val h = height * tileH
        val ox = xOffset * tileW
        val oy = yOffset * tileH

        val ax = hitboxCenter.ordinal % 3 * 0.5
        val ay = hitboxCenter.ordinal / 3 * 0.5

        val left = pos.x + ox - w * ax
        val top = pos.y + oy - h * ay

        return Rectangle(left, top, w, h)
    }

    // AABB intersection with a point
    fun contains(hitboxPosition: Position, point: Position, lawnType: LawnType): Boolean {
        val r = bounds(hitboxPosition, lawnType)
        return point.x in r.left..r.right && point.y in r.top..r.bottom
    }

    // AABB intersection with another hitbox
    fun contains(other: Hitbox, hitboxPosition: Position, otherHitboxPos: Position, lawnType: LawnType): Boolean {
        val r1 = this.bounds(hitboxPosition, lawnType)
        val r2 = other.bounds(otherHitboxPos, lawnType)

        return r1.left < r2.right &&
            r1.right > r2.left &&
            r1.top < r2.bottom &&
            r1.bottom > r2.top
    }
}
