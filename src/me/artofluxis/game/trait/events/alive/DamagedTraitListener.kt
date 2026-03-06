package me.artofluxis.game.trait.events.alive

import me.artofluxis.game.game.objects.LawnObject

interface DamagedTraitListener {
    fun damagedBy(damager: LawnObject)
}
