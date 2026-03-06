package projectile.traits

import game.objects.logic.*
import trait.*

class EffectApplierInstance(
    override val parent: LawnProjectile,
    override val trait: EffectApplierTrait
) : TraitInstance(parent, trait) {

}
