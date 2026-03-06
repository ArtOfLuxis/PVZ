package trait.events

import game.objects.logic.*

interface HitByProjectileTraitListener {
    fun hitByProjectile(projectile: LawnProjectile)
}
