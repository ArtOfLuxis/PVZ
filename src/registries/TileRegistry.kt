package registries

import korlibs.io.file.std.*
import kotlinx.serialization.json.*
import loadBitmap
import tile.*
import trait.*

data object TileRegistry : Registry {
    val tiles = HashMap<String, TileType>()

    override suspend fun load() {
        val text = resourcesVfs["data/tiles.json"].readString()
        val json = Json.parseToJsonElement(text).jsonArray

        for (value in json) {
            val id = value.jsonObject["id"]!!.jsonPrimitive.content
            val asset = value.jsonObject["asset"]!!.jsonPrimitive.contentOrNull
            if (asset != null) loadBitmap(asset)

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
