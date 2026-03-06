package trait.events

import game.objects.*
import game.objects.logic.*

interface ProjectileHitObjectTraitListener {
    fun hitObject(obj: LawnObject)
}
