package game.objects.logic

import Position
import effects.*
import game.hitbox.*
import game.objects.*
import game.scenes.*
import korlibs.korge.view.*
import plant.PlantType
import trait.*

class LawnPlant(
    override var pos: Position,
    override val row: Int,
    override val hitHitbox: Hitbox,
    override var team: ObjectTeam,
    override val scene: InGameScene,
    override var image: Image?,
    override val traits: HashSet<TraitInstance>,
    override val toughness: Double,
    val type: PlantType,
    val effects: MutableList<Effect>,
): AliveLawnObject {

    override fun asset() = type.spriteAsset
}
