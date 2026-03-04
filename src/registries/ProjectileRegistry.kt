package registries

import korlibs.io.file.std.*
import kotlinx.serialization.json.*
import projectile.*
import trait.*

object ProjectileRegistry {
    val projectiles = HashMap<String, ProjectileType>()

    suspend fun load() {
        val text = resourcesVfs["data/projectiles.json"].readString()
        val json = Json.parseToJsonElement(text).jsonArray

        for (value in json) {
            val id = value.jsonObject["id"]!!.jsonPrimitive.content
            val damage = value.jsonObject["damage"]!!.jsonPrimitive.int
            val asset = "textures/" + value.jsonObject["asset"]!!.jsonPrimitive.content

            val traits = hashSetOf<Trait>()
            value.jsonObject["traits"]!!.jsonArray.forEach { obj ->
                val traitID = obj.jsonObject["id"]!!.jsonPrimitive.content
                val trait = Trait.from(traitID, obj.jsonObject)

                if (trait.traitType != TraitType.PROJECTILE)
                    error("Non-projectile trait in a projectile definition\n$obj")
                traits.add(trait)
            }

            projectiles[id] = ProjectileType(id, damage, resourcesVfs[asset], traits)
        }
    }

    fun get(id: String): ProjectileType =
        projectiles[id] ?: error("Unknown projectile type: $id")
}
