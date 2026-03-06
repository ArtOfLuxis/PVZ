package game.objects

import game.objects.logic.*
import trait.events.*

interface AliveLawnObject : TickableLawnObject {
    val toughness: Double

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
