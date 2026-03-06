package game.objects

import Position
import game.hitbox.*
import game.scenes.*
import korlibs.korge.scene.*
import korlibs.korge.view.*

interface LawnObject {
    var pos: Position
    val row: Int
    val hitHitbox: Hitbox
    var team: ObjectTeam
    val scene: InGameScene
    var image: Image?

    fun asset(): String
    fun findIntersectingObjects(filter: (LawnObject, Hitbox) -> Boolean): List<LawnObject> {
        return scene.lawnObjects.filter { other ->
            other != this &&
                this.hitHitbox.contains(
                    other.hitHitbox,
                    this.pos,
                    other.pos,
                    scene.lawnType
                ) &&
                filter(other, other.hitHitbox)
        }
    }
}
