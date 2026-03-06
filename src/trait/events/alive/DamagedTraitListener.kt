package trait.events.alive

import game.objects.*

interface DamagedTraitListener {
    fun damagedBy(damager: LawnObject)
}
