package me.artofluxis.game.game.objects

import me.artofluxis.game.*
import me.artofluxis.game.game.hitbox.*

interface LocationalLawnObject: LawnObject {
    var team: ObjectTeam?
    var pos: Position
    val row: Int

    fun scale() = scene.lawnType.teamSizes[team]
        ?: error("Unknown scale for team $team on lawn ${scene.lawnType}")

    fun offset() = scene.lawnType.teamOffsets[team]
        ?: error("Unknown offset for team $team on lawn ${scene.lawnType}")

    fun hitHitbox(): Hitbox?


    fun findIntersectingObjects(filter: (LocationalLawnObject, Hitbox) -> Boolean): List<LocationalLawnObject> {
        return (this.hitHitbox() ?: error(
            "Tried to find intersecting objects from an object without a hitHitbox"
        )).findIntersectingObjects(this, filter)
    }

    fun isIntersecting(
        hitbox: Hitbox,
        otherObj: LocationalLawnObject,
        condition: (LocationalLawnObject, Hitbox) -> Boolean
    ): Boolean {
        return (this.hitHitbox() ?: error(
            "Tried to find intersecting objects from an object without a hitHitbox"
        )).isIntersecting(this, otherObj, this.scene.lawnType, condition)
    }

    fun isIntersecting(
        hitbox: Hitbox,
        otherObj: LocationalLawnObject,
    ) = this.isIntersecting(hitbox, otherObj) { _, _ -> true}
}
