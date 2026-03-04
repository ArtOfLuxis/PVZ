package trait

import kotlinx.serialization.*
import kotlinx.serialization.json.*
import plant.traits.*
import projectile.traits.*
import tile.traits.*

open class Trait(
    private val fields: Map<String, KSerializer<*>>,
    val traitType: TraitType
) {

    fun deserialize(json: JsonObject, jsonFormat: Json = Json): HashMap<String, Any> {
        if (!json.containsKey("id")) error("Missing required key: id")

        val result = HashMap<String, Any>()

        for ((key, serializer) in fields) {
            val element = json[key] ?: error("Missing required key: $key")
            val value = jsonFormat.decodeFromJsonElement(serializer, element)!!
            result[key] = value
        }

        result["id"] = json["id"]!!.jsonPrimitive.content
        return result
    }

    companion object {
        fun from(name: String, json: JsonObject): Trait {
            return when (name) {
                // plant traits
                "StraightShooter" -> ::StraightShooterTrait

                // projectile traits
                "EffectApplier" -> ::EffectApplierTrait

                // zombie traits


                // tile traits
                "EffectApplierTile" -> ::EffectApplierTileTrait

                // unknown
                else -> error("Unknown trait: $name")
            }.invoke(json)
        }
    }
}
