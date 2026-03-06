package game.objects

import Timer
import effects.*
import game.objects.logic.*
import trait.events.alive.*

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
