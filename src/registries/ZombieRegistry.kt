package registries

import game.types.*
import korlibs.io.file.std.*
import kotlinx.serialization.json.*
import loadBitmap
import trait.*

data object ZombieRegistry : Registry {
    val zombies = HashMap<String, ZombieType>()

    override suspend fun load() {
        val text = resourcesVfs["data/zombies.json"].readString()
        val json = Json.parseToJsonElement(text).jsonArray

        for (value in json) {
            val id = value.jsonObject["id"]!!.jsonPrimitive.content
            val name = value.jsonObject["name"]!!.jsonPrimitive.content
            val toughness = value.jsonObject["toughness"]!!.jsonPrimitive.double
            val hitHitbox = HitboxRegistry.get(value.jsonObject["hitHitbox"]!!.jsonPrimitive.content)
            val asset = value.jsonObject["asset"]!!.jsonPrimitive.content
            loadBitmap(asset)

            val traits = hashSetOf<Trait>()
            value.jsonObject["traits"]!!.jsonArray.forEach { obj ->
                val traitID = obj.jsonObject["id"]!!.jsonPrimitive.content
                val trait = Trait.from(traitID, obj.jsonObject)

                if (trait.traitType != TraitType.ZOMBIE)
                    error("Non-zombie trait in a plant definition\n$obj")
                traits.add(trait)
            }

            zombies[id] = ZombieType(
                id, name, toughness, hitHitbox, asset, traits
            )
        }
    }

    fun get(id: String): ZombieType =
        zombies[id] ?: error("Unknown zombie id: $id")
}
