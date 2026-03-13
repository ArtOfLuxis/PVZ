package me.artofluxis.game.game.objects.logic

import me.artofluxis.game.HighlightFilter
import me.artofluxis.game.Position
import me.artofluxis.game.Timer
import korlibs.korge.view.*
import me.artofluxis.game.animation.*
import me.artofluxis.game.effects.*
import me.artofluxis.game.game.hitbox.*
import me.artofluxis.game.game.objects.*
import me.artofluxis.game.game.scenes.*
import me.artofluxis.game.game.types.*
import me.artofluxis.game.trait.*

@Suppress("MemberVisibilityCanBePrivate")
class LawnZombie(
    override var pos: Position,
    override val row: Int,
    override var team: ObjectTeam?,
    override val scene: InGameScene,
    override var image: Image?,
    override val animationPlayer: AnimationPlayer,
    override val traits: HashSet<TraitInstance>,
    override val effects: HashMap<Effect, Timer>,
    val type: ZombieType,
): AliveLawnObject() {
    override val highlightFilter = HighlightFilter(mutableListOf())

    override fun hitHitbox() = type.hitHitbox

    override fun toString(): String = "${this::class.simpleName}[${type.id}]"
}
