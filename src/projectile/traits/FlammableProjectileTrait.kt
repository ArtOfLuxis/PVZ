package projectile.traits

import game.objects.*
import game.objects.logic.*
import kotlinx.serialization.json.*
import trait.*

class FlammableProjectileTrait(
    jsonObject: JsonObject
) : Trait(hashMapOf(
        //"effects" to String.serializer() // make a separate class soon
), TraitType.PROJECTILE) {


    override fun createInstance(parent: LawnObject): TraitInstance {
        require(parent is LawnProjectile) {
            "Parent for ${this::class.simpleName} must be a ${LawnProjectile::class.simpleName}, found a ${parent::class.simpleName}"
        }
        return FlammableProjectileInstance(parent, this)
    }
}
