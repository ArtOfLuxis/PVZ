package plant.traits

import Position
import game.objects.*
import game.objects.logic.*
import kotlinx.serialization.builtins.*
import kotlinx.serialization.json.*
import projectile.*
import serializers.*
import trait.*

class StraightShooterTrait(
    jsonObject: JsonObject
) : Trait(
    mapOf(
        "projectile" to ProjectileTypeSerializer,
        "projectilePositionOffset" to PositionSerializer,
        "interval" to Double.serializer(),
        "additionalInterval" to Double.serializer(),
        "detectionHitbox" to HitboxSerializer,
        "projectileAmount" to Int.serializer(),
        "projectileInterval" to Double.serializer(),
        "attackRows" to ListSerializer(Int.serializer()),
    ),
    TraitType.PLANT
) {
    override val values = deserialize(jsonObject)

    val projectile get() = get<ProjectileType>("projectile")
    val projectilePositionOffset get() = get<Position>("projectilePositionOffset")
    val interval get() = get<Double>("interval")
    val additionalInterval get() = get<Double>("additionalInterval")
    val projectileAmount get() = get<Int>("projectileAmount")
    val projectileInterval get() = get<Double>("projectileInterval")
    val attackRows get() = get<List<Int>>("attackRows")

    override fun createInstance(parent: LawnObject): TraitInstance {
        require(parent is LawnPlant) {
            "Parent for ${this::class.simpleName} must be a ${LawnPlant::class.simpleName}, found a ${parent::class.simpleName}"
        }
        return StraightShooterInstance(parent, this)
    }
}
