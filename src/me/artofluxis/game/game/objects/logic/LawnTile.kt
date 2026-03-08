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
    override val scene: InGameScene,
    override var image: Image?,
    override val traits: HashSet<TraitInstance>,
    val type: TileType,
): TickableLawnObject() {
    override var team: ObjectTeam? = null
    override val highlightFilter = HighlightFilter(mutableListOf())

    override fun asset() = type.asset

    override fun hitHitbox() = null

    override fun toString(): String = "${this::class.simpleName}[${type.id}]"

    override fun scale() = 1.0
    override fun offset() = 0.0 to 0.0
}
