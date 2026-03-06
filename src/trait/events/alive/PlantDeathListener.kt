package trait.events.alive

import game.objects.*

interface PlantDeathListener {
    fun onPlantDeath(damager: LawnObject)
}
