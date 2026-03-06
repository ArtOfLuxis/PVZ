package me.artofluxis.game.game.objects

import me.artofluxis.game.HighlightFilter
import me.artofluxis.game.Position
import korlibs.korge.view.*
import korlibs.korge.view.filter.*
import me.artofluxis.game.game.hitbox.*
import me.artofluxis.game.game.scenes.*

interface LawnObject {
    var pos: Position
    val row: Int
    val hitHitbox: Hitbox
    var team: ObjectTeam
    val scene: InGameScene
    var image: Image?
    val highlightFilter: HighlightFilter

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

    fun setNewImage(newImage: Image) {
        this.image = newImage
        this.image!!.filter = highlightFilter
    }
}
