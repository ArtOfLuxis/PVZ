package game

import Position
import effects.*
import game.hitbox.*
import korlibs.io.file.*
import plant.PlantType

class OnLawnPlant(
    override val pos: Position,
    override val hitHitbox: Hitbox,
    override var team: ObjectTeam,
    val type: PlantType,
    val toughness: Int,
    val effects: List<EffectType>
): OnLawnObject {

    override fun asset() = type.spriteAsset

}
