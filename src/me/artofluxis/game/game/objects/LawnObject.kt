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
    var team: ObjectTeam
    val scene: InGameScene
    var image: Image?
    val highlightFilter: HighlightFilter

    fun asset(): String?

    fun setNewImage(newImage: Image) {
        this.image = newImage
        this.image!!.filter = highlightFilter
    }

    fun scale() = scene.lawnType.teamSizes[team]
        ?: error("Unknown scale for team $team on lawn ${scene.lawnType}")

    fun offset() = scene.lawnType.teamOffsets[team]
        ?: error("Unknown offset for team $team on lawn ${scene.lawnType}")
}
