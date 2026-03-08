package me.artofluxis.game.trait.events.alive

import me.artofluxis.game.game.objects.logic.LawnProjectile
import me.artofluxis.game.trait.*

interface HitByProjectileTraitListener: TraitTrigger {
    fun hitByProjectile(projectile: LawnProjectile, damage: Double)
}
