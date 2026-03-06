package trait.events.alive

import game.objects.logic.*

interface HitByProjectileTraitListener {
    fun hitByProjectile(projectile: LawnProjectile)
}
