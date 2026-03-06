package me.artofluxis.game.trait.events.alive

import me.artofluxis.game.game.objects.LawnObject

interface PlantDeathListener {
    fun onPlantDeath(damager: LawnObject)
}
