package projectile.traits

import game.objects.*
import game.objects.logic.*
import kotlinx.serialization.builtins.*
import kotlinx.serialization.json.*
import trait.*

class EffectApplierTrait(
    jsonObject: JsonObject
) : Trait(hashMapOf(
        "effect" to String.serializer() // make a separate class soon
), TraitType.PROJECTILE) {
    override val values = deserialize(jsonObject)

    // val effect = get<>()

    override fun createInstance(parent: LawnObject): TraitInstance {
        require(parent is LawnProjectile) {
            "Parent for ${this::class.simpleName} must be a ${LawnProjectile::class.simpleName}, found a ${parent::class.simpleName}"
        }
        return EffectApplierInstance(parent, this)
    }
}
