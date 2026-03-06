package trait.events

import game.objects.*
import game.objects.logic.*

interface DamagedTraitListener {
    fun damagedBy(damager: LawnObject)
}
