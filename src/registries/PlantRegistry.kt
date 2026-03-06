package registries

import game.types.*
import korlibs.io.file.std.*
import kotlinx.serialization.json.*
import loadBitmap
import trait.*

data object PlantRegistry : Registry {
    val plants = HashMap<String, PlantType>()

    override suspend fun load() {
        val text = resourcesVfs["data/plants.json"].readString()
        val json = Json.parseToJsonElement(text).jsonArray

        for (value in json) {
            val id = value.jsonObject["id"]!!.jsonPrimitive.content
            val name = value.jsonObject["name"]!!.jsonPrimitive.content
            val sunCost = value.jsonObject["sunCost"]!!.jsonPrimitive.int
            val refreshTime = value.jsonObject["refreshTime"]!!.jsonPrimitive.double
            val toughness = value.jsonObject["toughness"]!!.jsonPrimitive.double
            val hitHitbox = HitboxRegistry.get(value.jsonObject["hitHitbox"]!!.jsonPrimitive.content)
            val spriteAsset = value.jsonObject["sprite"]!!.jsonPrimitive.content
            val packetAsset = value.jsonObject["spritePacket"]!!.jsonPrimitive.content
            loadBitmap(spriteAsset)
            loadBitmap(packetAsset)

            val traits = hashSetOf<Trait>()
            value.jsonObject["traits"]!!.jsonArray.forEach { obj ->
                val traitID = obj.jsonObject["id"]!!.jsonPrimitive.content
                val trait = Trait.from(traitID, obj.jsonObject)

                if (trait.traitType != TraitType.PLANT)
                    error("Non-plant trait in a plant definition\n$obj")
                traits.add(trait)
            }

            plants[id] = PlantType(
                id, name, sunCost, refreshTime, toughness,
                hitHitbox, spriteAsset, packetAsset,
                traits
            )
        }
    }

    fun get(id: String): PlantType =
        plants[id] ?: error("Unknown plant id: $id")
}
