package me.artofluxis.game.game.objects.logic

import korlibs.korge.view.*
import me.artofluxis.game.*
import me.artofluxis.game.animation.*
import me.artofluxis.game.game.objects.*
import me.artofluxis.game.game.scenes.*
import me.artofluxis.game.game.types.*
import me.artofluxis.game.mod.trait.*

class Lawn(
    override val scene: InGameScene,
    override var image: Image?,
    override val animationPlayer: AnimationPlayer?,
    override val traits: HashSet<TraitInstance>,
    val type: LawnType,
): TickableLawnObject() {
    override val highlightFilter = HighlightFilter(mutableListOf())

    override fun toString(): String = "${this::class.simpleName}[${type.id}]"
}
