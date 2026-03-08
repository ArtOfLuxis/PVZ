package me.artofluxis.game.trait.events.alive

import me.artofluxis.game.trait.*

interface ObjectCreatedTraitListener: TraitTrigger {
    fun onCreation()
}
