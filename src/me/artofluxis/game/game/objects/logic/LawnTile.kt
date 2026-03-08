package me.artofluxis.game.game.objects.logic

import korlibs.korge.view.*
import me.artofluxis.game.*
import me.artofluxis.game.game.hitbox.*
import me.artofluxis.game.game.objects.*
import me.artofluxis.game.game.scenes.*
import me.artofluxis.game.game.types.*
import me.artofluxis.game.trait.*

@Suppress("MemberVisibilityCanBePrivate")
class LawnTile(
    override var pos: Position,
    override val row: Int,
    override var team: ObjectTeam,
    override val scene: InGameScene,
    override var image: Image?,
    override val traits: HashSet<TraitInstance>,
    val type: TileType,
): TickableLawnObject() {
    override val highlightFilter = HighlightFilter(mutableListOf())

    override fun asset() = type.asset

    override fun toString(): String = "${this::class.simpleName}[${type.id}]"

    override fun offset(): Pair<Double, Double> = 0.0 to 0.0
}
