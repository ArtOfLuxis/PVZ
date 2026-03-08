package me.artofluxis.game.trait.events.alive

import me.artofluxis.game.game.objects.LawnObject
import me.artofluxis.game.trait.*

interface DamagedTraitListener: TraitTrigger {
    fun damagedBy(damager: LawnObject, damage: Double)
}
