package game.objects.logic

import Position
import effects.*
import game.hitbox.*
import game.objects.*
import game.scenes.*
import korlibs.korge.view.*
import trait.*
import zombie.*

class LawnZombie(
    override var pos: Position,
    override val row: Int,
    override val hitHitbox: Hitbox,
    override var team: ObjectTeam,
    override val scene: InGameScene,
    override var image: Image?,
    override val traits: HashSet<TraitInstance>,
    override val toughness: Double,
    val type: ZombieType,
    val effects: List<Effect>
): AliveLawnObject {

    override fun asset() = type.asset
}
