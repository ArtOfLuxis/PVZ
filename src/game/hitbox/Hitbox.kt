package game.hitbox

import Position

class Hitbox(
    val id: String,
    val width: Double,
    val height: Double,
    val xOffset: Double,
    val yOffset: Double,
    val hitboxCenter: HitboxCenter
) {

    operator fun contains(pos: Position): Boolean {
        val ax = hitboxCenter.ordinal % 3 * 0.5
        val ay = hitboxCenter.ordinal / 3 * 0.5

        val left = xOffset - width * ax
        val top = yOffset - height * ay
        val right = left + width
        val bottom = top + height

        return pos.x in left..right && pos.y in top..bottom
    }

    operator fun contains(other: Hitbox): Boolean {
        // compute this hitbox bounds
        val ax1 = hitboxCenter.ordinal % 3 * 0.5
        val ay1 = hitboxCenter.ordinal / 3 * 0.5
        val left1 = xOffset - width * ax1
        val top1 = yOffset - height * ay1
        val right1 = left1 + width
        val bottom1 = top1 + height

        // compute other hitbox bounds
        val ax2 = other.hitboxCenter.ordinal % 3 * 0.5
        val ay2 = other.hitboxCenter.ordinal / 3 * 0.5
        val left2 = other.xOffset - other.width * ax2
        val top2 = other.yOffset - other.height * ay2
        val right2 = left2 + other.width
        val bottom2 = top2 + other.height

        // check if all corners of 'other' are inside this hitbox
        return left2 >= left1 &&
            right2 <= right1 &&
            top2 >= top1 &&
            bottom2 <= bottom1
    }
}
