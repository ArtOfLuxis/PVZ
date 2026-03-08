package me.artofluxis.game.game.objects

import me.artofluxis.game.game.objects.logic.*
import me.artofluxis.game.trait.events.alive.*

abstract class DamageableLawnObject : TickableLawnObject() {
    fun hitByProjectile(projectile: LawnProjectile, damage: Double) {
        val snapshot = getTraitsSnapshot()
        for (it in snapshot) {
            if (it is HitByProjectileTraitListener) it.hitByProjectile(projectile, damage)
            if (it is DamagedTraitListener) it.damagedBy(projectile, damage)
        }
    }

    fun damagedByZombie(zombie: LawnZombie, damage: Double) {
        val snapshot = getTraitsSnapshot()
        for (it in snapshot) {
            if (it is DamagedByZombieTraitListener) { it.damagedByZombie(zombie, damage) }
            if (it is DamagedTraitListener) { it.damagedBy(zombie, damage) }
        }
    }

    fun death(killer: LawnObject) {
        val snapshot = getTraitsSnapshot()
        for (it in snapshot) {
            if (it is DeathTraitListener) { it.onObjectDeath(killer) }
        }
    }
}
