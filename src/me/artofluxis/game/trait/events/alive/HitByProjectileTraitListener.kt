package me.artofluxis.game.trait.events.alive

import me.artofluxis.game.game.objects.logic.LawnProjectile

interface HitByProjectileTraitListener {
    fun hitByProjectile(projectile: LawnProjectile)
}
