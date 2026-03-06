package trait.events.alive

import game.objects.*

interface ProjectileHitObjectTraitListener {
    fun projectileHitObject(obj: LawnObject)
}
