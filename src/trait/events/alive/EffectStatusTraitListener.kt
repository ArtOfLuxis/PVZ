package trait.events.alive

import effects.*

interface EffectStatusTraitListener {
    fun appliedEffect(effect: Effect, effectTime: Double)
    fun removedEffect(effect: Effect)
}
