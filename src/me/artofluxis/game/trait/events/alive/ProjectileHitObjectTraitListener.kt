package me.artofluxis.game.trait.events.alive

import me.artofluxis.game.game.objects.LawnObject

interface ProjectileHitObjectTraitListener {
    fun projectileHitObject(obj: LawnObject)
}
