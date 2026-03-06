package game.objects.logic

import HighlightFilter
import Position
import Timer
import effects.*
import game.hitbox.*
import game.objects.*
import game.scenes.*
import game.types.*
import korlibs.korge.view.*
import trait.*

class LawnZombie(
    override var pos: Position,
    override val row: Int,
    override val hitHitbox: Hitbox,
    override var team: ObjectTeam,
    override val scene: InGameScene,
    override var image: Image?,
    override val traits: HashSet<TraitInstance>,
    override val toughness: Double,
    override val effects: HashMap<Effect, Timer>,
    val type: ZombieType,
): AliveLawnObject {
    override val highlightFilter = HighlightFilter(mutableListOf())

    override fun asset() = type.asset
}
