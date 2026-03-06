package me.artofluxis.game.trait.events.alive

import me.artofluxis.game.effects.Effect

interface EffectStatusTraitListener {
    fun appliedEffect(effect: Effect, effectTime: Double)
    fun removedEffect(effect: Effect)
}
