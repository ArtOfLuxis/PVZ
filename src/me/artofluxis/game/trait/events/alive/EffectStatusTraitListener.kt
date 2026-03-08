package me.artofluxis.game.trait.events.alive

import me.artofluxis.game.effects.Effect
import me.artofluxis.game.trait.*

interface EffectStatusTraitListener: TraitTrigger {
    fun appliedEffect(effect: Effect, effectTime: Double)
    fun removedEffect(effect: Effect)
}
