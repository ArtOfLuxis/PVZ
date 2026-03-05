package game

import Position
import game.hitbox.*
import korlibs.io.file.*

interface OnLawnObject {
    val pos: Position
    val hitHitbox: Hitbox
    var team: ObjectTeam

    fun asset(): VfsFile
}
