package me.artofluxis.game.registries

import korlibs.image.bitmap.*
import korlibs.io.file.std.*
import kotlinx.serialization.json.*
import me.artofluxis.game.*
import me.artofluxis.game.animation.*
import me.artofluxis.game.game.types.*
import me.artofluxis.game.trait.*
import java.io.*

data object PlantRegistry : Registry {
    val plants = HashMap<String, PlantType>()

    override suspend fun load() {
        val text = File(dataFolder, "plants.json").readText()
        val json = Json.parseToJsonElement(text).jsonArray

        for (value in json) {
            val id = value.jsonObject["id"]!!.jsonPrimitive.content
            val name = value.jsonObject["name"]!!.jsonPrimitive.content
            val hitHitbox = HitboxRegistry.get(value.jsonObject["hitHitbox"]!!.jsonPrimitive.content)
            val sunCost = value.jsonObject["sunCost"]!!.jsonPrimitive.int
            val refreshTime = value.jsonObject["refreshTime"]!!.jsonPrimitive.double
            val scale = value.jsonObject["imageScale"]!!.jsonPrimitive.double

            val animationPackPath = value.jsonObject["animationPack"]!!.jsonPrimitive.content
            val animationPack = animationPackFromPath(animationPackPath)

            val packetAsset = value.jsonObject["spritePacket"]!!.jsonPrimitive.content
            BitmapLoader.loadBitmap(packetAsset)

            val traits = hashSetOf<Trait>()
            value.jsonObject["traits"]!!.jsonArray.forEach { obj ->
                val traitID = obj.jsonObject["id"]!!.jsonPrimitive.content
                val trait = Trait.from(traitID, obj.jsonObject)

                if (trait.traitType != TraitType.PLANT && trait.traitType != TraitType.GENERIC)
                    error("Non-plant trait in a plant definition $obj")
                traits.add(trait)
            }

            plants[id] = PlantType(
                id, name, hitHitbox, sunCost, refreshTime,
                scale, animationPack, packetAsset, traits
            )
        }
    }

    fun get(id: String): PlantType =
        plants[id] ?: error("Unknown plant id: $id")
}
