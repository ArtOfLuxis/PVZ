package me.artofluxis.game.trait.events.general

import me.artofluxis.game.trait.*

interface TickTraitListener: TraitTrigger {
    fun tick(deltaTime: Double)
}
