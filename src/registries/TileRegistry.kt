package registries

import korlibs.io.file.std.*
import kotlinx.serialization.json.*
import tile.*
import trait.*

object TileRegistry {
    val tiles = HashMap<String, TileType>()

    suspend fun load() {
        val text = resourcesVfs["data/tiles.json"].readString()
        val json = Json.parseToJsonElement(text).jsonArray

        for (value in json) {
            val id = value.jsonObject["id"]!!.jsonPrimitive.content
            val assetPath = value.jsonObject["asset"]!!.jsonPrimitive.contentOrNull
            val asset = if (assetPath == null) null else resourcesVfs["textures/$assetPath"]

            val traits = hashSetOf<Trait>()
            value.jsonObject["traits"]!!.jsonArray.forEach { obj ->
                val traitID = obj.jsonObject["id"]!!.jsonPrimitive.content
                val trait = Trait.from(traitID, obj.jsonObject)

                if (trait.traitType != TraitType.TILE)
                    error("Non-tile trait in a plant definition\n$obj")
                traits.add(trait)
            }

            tiles[id] = TileType(id, asset, traits)
        }
    }

    fun get(id: String): TileType =
        tiles[id] ?: error("Unknown tile id: $id")
}
