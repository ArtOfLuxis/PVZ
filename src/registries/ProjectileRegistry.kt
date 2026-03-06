package registries

import game.types.*
import korlibs.io.file.std.*
import kotlinx.serialization.json.*
import loadBitmap
import trait.*

data object ProjectileRegistry : Registry {
    val projectiles = HashMap<String, ProjectileType>()

    override suspend fun load() {
        val text = resourcesVfs["data/projectiles.json"].readString()
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
