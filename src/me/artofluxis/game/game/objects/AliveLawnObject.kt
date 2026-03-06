package me.artofluxis.game.game.objects

import me.artofluxis.game.Timer
import me.artofluxis.game.effects.*
import me.artofluxis.game.game.objects.logic.*
import me.artofluxis.game.trait.events.alive.*

interface AliveLawnObject : TickableLawnObject {
    val toughness: Double
    val effects: HashMap<Effect, Timer>

    fun applyEffect(effect: Effect, effectTime: Double) {
        traits.forEach {
            if (it is EffectStatusTraitListener) it.appliedEffect(effect, effectTime)
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
            .sortedBy { it.operationOrder.ordinal }

        modifiers.forEach {
            value = it.operationType.operation(value, it.value)
        }

        return value
    }

    fun hitByProjectile(projectile: LawnProjectile) {
        traits.forEach {
            if (it is HitByProjectileTraitListener) { it.hitByProjectile(projectile) }
            if (it is DamagedTraitListener) { it.damagedBy(projectile) }
        }
    }

    fun damagedByZombie(zombie: LawnZombie) {
        traits.forEach {
            if (it is DamagedByZombieTraitListener) { it.damagedByZombie(zombie) }
            if (it is DamagedTraitListener) { it.damagedBy(zombie) }
        }
    }
}
