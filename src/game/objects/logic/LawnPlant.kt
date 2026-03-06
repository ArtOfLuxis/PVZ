package game.objects.logic

import HighlightFilter
import Position
import Timer
import effects.*
import game.hitbox.*
import game.objects.*
import game.scenes.*
import korlibs.korge.view.*
import game.types.PlantType
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
    override val effects: HashMap<Effect, Timer>,
    val type: PlantType,
): AliveLawnObject {
    override val highlightFilter = HighlightFilter(mutableListOf())

    override fun asset() = type.spriteAsset
}
