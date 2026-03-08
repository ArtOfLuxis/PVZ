package me.artofluxis.game.game.objects

import me.artofluxis.game.*
import me.artofluxis.game.effects.*
import me.artofluxis.game.game.hitbox.*
import me.artofluxis.game.trait.events.alive.*

abstract class AliveLawnObject(
    hitHitbox: Hitbox
) : DamageableLawnObject(hitHitbox) {
    open val effects = hashMapOf<Effect, Timer>()

    fun applyEffect(effect: Effect, effectTime: Double) {
        traits.forEach {
            if (it is EffectStatusTraitListener) it.appliedEffect(effect, effectTime)
        }
    }

    fun removeEffect(effect: Effect) {
        traits.forEach {
            if (it is EffectStatusTraitListener) it.removedEffect(effect)
        }
    }

    fun getStat(
        baseValue: Double,
        statType: EffectModifierType
    ): Double {
        var value = baseValue

        val modifiers = effects
            .flatMap { it.key.effects }
            .filter { it.type == statType }
            .sortedBy { it.operationOrder.order }

        modifiers.forEach {
            value = it.operationType.operation(value, it.value)
        }

        return value
    }
}
