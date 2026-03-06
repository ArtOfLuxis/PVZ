package me.artofluxis.game.registries

import me.artofluxis.game.dataFolder
import kotlinx.serialization.json.*
import me.artofluxis.game.loadBitmap
import me.artofluxis.game.game.types.*
import me.artofluxis.game.trait.*
import java.io.*

data object ProjectileRegistry : Registry {
    val projectiles = HashMap<String, ProjectileType>()

    override suspend fun load() {
        val text = File(dataFolder, "projectiles.json").readText()
        val json = Json.parseToJsonElement(text).jsonArray

        for (value in json) {
            val id = value.jsonObject["id"]!!.jsonPrimitive.content
            val asset = value.jsonObject["asset"]!!.jsonPrimitive.content
            loadBitmap(asset)
            val damage = value.jsonObject["damage"]!!.jsonPrimitive.int
            val detectionHitbox =
                HitboxRegistry.get(value.jsonObject["detectionHitbox"]!!.jsonPrimitive.content)

            val traits = hashSetOf<Trait>()
            value.jsonObject["traits"]!!.jsonArray.forEach { obj ->
                val traitID = obj.jsonObject["id"]!!.jsonPrimitive.content
                val trait = Trait.from(traitID, obj.jsonObject)

                if (trait.traitType != TraitType.PROJECTILE)
                    error("Non-projectile trait in a projectile definition\n$obj")
                traits.add(trait)
            }

            projectiles[id] = ProjectileType(id, asset, damage, detectionHitbox, traits)
        }
    }

    fun get(id: String): ProjectileType =
        projectiles[id] ?: error("Unknown projectile id: $id")
}
