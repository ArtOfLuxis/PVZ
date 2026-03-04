package plant.traits

import kotlinx.serialization.builtins.*
import kotlinx.serialization.json.*
import projectile.*
import serializers.*
import trait.*

@Suppress("UNCHECKED_CAST")
class StraightShooterTrait(
    jsonObject: JsonObject
) : Trait(hashMapOf(
    "projectile" to ProjectileTypeSerializer,
    "interval" to Double.serializer(),
    "additionalInterval" to Double.serializer(),
    "projectileAmount" to Int.serializer(),
    "projectileInterval" to Double.serializer(),
    "attackRows" to ListSerializer(Int.serializer()),
    "projectileInEachRow" to Boolean.serializer(),
), TraitType.PLANT) {
    private val projectile: ProjectileType
    private val interval: Double
    private val additionalInterval: Double
    private val projectileAmount: Int
    private val projectileInterval: Double
    private val attackRows: List<Int>
    private val projectileInEachRow: Boolean

    init {
        val values = deserialize(jsonObject)
        projectile = values["projectile"] as ProjectileType
        interval = values["interval"] as Double
        additionalInterval = values["additionalInterval"] as Double
        projectileAmount = values["projectileAmount"] as Int
        projectileInterval = values["projectileInterval"] as Double
        attackRows = values["attackRows"] as List<Int>
        projectileInEachRow = values["projectileInEachRow"] as Boolean
    }

    override fun toString() = """
        StraightShooterTrait[
            projectile=$projectile, 
            interval=$interval,
            additionalInterval=$additionalInterval,
            projectileAmount=$projectileAmount,
            projectileInterval=$projectileInterval,
            attackRows=$attackRows,
            projectileInEachRow=$projectileInEachRow
        ]
    """.trimIndent()
}
