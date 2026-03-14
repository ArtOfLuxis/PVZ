package me.artofluxis.game.registries

import kotlinx.serialization.json.*
import me.artofluxis.game.*
import me.artofluxis.game.game.types.*
import me.artofluxis.game.mod.trait.Trait
import me.artofluxis.game.mod.trait.TraitType
import java.io.*

data object ProjectileRegistry : Registry {
    val projectiles = HashMap<String, ProjectileType>()

    override suspend fun load() {
        val text = File(dataFolder, "projectiles.json").readText()
        val json = Json.parseToJsonElement(text).jsonArray

        for (value in json) {
            val id = value.jsonObject["id"]!!.jsonPrimitive.content

            val animationPackPath = value.jsonObject["animationPack"]!!.jsonPrimitive.content
            val animationPack = animationPackFromPath(animationPackPath)

            val detectionHitbox = HitboxRegistry.get(value.jsonObject["detectionHitbox"]!!.jsonPrimitive.content)
            val traits = hashSetOf<Trait>()
            value.jsonObject["traits"]!!.jsonArray.forEach { obj ->
                val traitID = obj.jsonObject["id"]!!.jsonPrimitive.content
                val trait = Trait.from(traitID, obj.jsonObject)

                if (trait.traitType != TraitType.PROJECTILE && trait.traitType != TraitType.GENERIC)
                    error("Non-projectile trait in a projectile definition $obj")
                traits.add(trait)
            }

            projectiles[id] = ProjectileType(id, animationPack, detectionHitbox, traits)
        }
    }

    fun get(id: String): ProjectileType =
        projectiles[id] ?: error("Unknown projectile id: $id")
}
