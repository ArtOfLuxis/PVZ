package me.artofluxis.game.registries

import kotlinx.serialization.json.*
import me.artofluxis.game.*
import me.artofluxis.game.game.types.*
import me.artofluxis.game.mod.trait.*
import java.io.*
import kotlin.collections.HashMap
import kotlin.collections.forEach
import kotlin.collections.hashSetOf
import kotlin.collections.set

data object ZombieRegistry : Registry {
    val zombies = HashMap<String, ZombieType>()

    override suspend fun load() {
        val text = File(dataFolder, "zombies.json").readText()
        val json = Json.parseToJsonElement(text).jsonArray

        for (value in json) {
            val id = value.jsonObject["id"]!!.jsonPrimitive.content
            val name = value.jsonObject["name"]!!.jsonPrimitive.content
            val hitHitbox = HitboxRegistry.get(value.jsonObject["hitHitbox"]!!.jsonPrimitive.content)
            val scale = value.jsonObject["imageScale"]!!.jsonPrimitive.double

            val animationPackPath = value.jsonObject["animationPack"]!!.jsonPrimitive.content
            val animationPack = animationPackFromPath(animationPackPath)

            val traits = hashSetOf<Trait>()
            value.jsonObject["traits"]!!.jsonArray.forEach { obj ->
                val traitID = obj.jsonObject["id"]!!.jsonPrimitive.content
                val trait = Trait.from(traitID, obj.jsonObject)

                if (trait.traitType != TraitType.ZOMBIE && trait.traitType != TraitType.GENERIC)
                    error("Non-zombie trait in a zombie definition $obj")
                traits.add(trait)
            }

            zombies[id] = ZombieType(
                id, name, hitHitbox, scale, animationPack, traits
            )
        }
    }

    fun get(id: String): ZombieType =
        zombies[id] ?: error("Unknown zombie id: $id")
}
