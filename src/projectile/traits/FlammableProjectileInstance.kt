package projectile.traits

import game.objects.logic.*
import trait.*

class FlammableProjectileInstance(
    override val parent: LawnProjectile,
    override val trait: FlammableProjectileTrait
) : TraitInstance(parent, trait) {

}
