package zombie.traits

import game.objects.*
import game.objects.logic.*
import kotlinx.serialization.builtins.*
import kotlinx.serialization.json.*
import projectile.traits.*
import trait.*
import trait.events.*

class CommonZombieLogicTrait(
    jsonObject: JsonObject
) : Trait(hashMapOf(
    "eatDPS" to Double.serializer(),
    "speed" to Double.serializer(),
), TraitType.ZOMBIE) {
    override val values = deserialize(jsonObject)

    private val eatDPS = get<Double>("eatDPS")
    private val speed = get<Double>("speed")

    override fun createInstance(parent: LawnObject): TraitInstance {
        require(parent is LawnZombie) {
            "Parent for ${this::class.simpleName} must be a ${LawnZombie::class.simpleName}, found a ${parent::class.simpleName}"
        }
        return CommonZombieLogicInstance(parent, this)
    }
}
