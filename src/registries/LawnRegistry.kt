package registries

import korlibs.io.file.std.*
import kotlinx.serialization.json.*
import lawn.*
import tile.*

object LawnRegistry {
    val lawns = HashMap<String, LawnType>()

    suspend fun load() {
        val text = resourcesVfs["data/lawns.json"].readString()
        val json = Json.parseToJsonElement(text).jsonArray

        for (value in json) {
            val id = value.jsonObject["id"]!!.jsonPrimitive.content
            val asset = "textures/" + value.jsonObject["asset"]!!.jsonPrimitive.content
            val sunSprite = SpriteRegistry.get(value.jsonObject["sunSprite"]!!.jsonPrimitive.content)
            val rows = value.jsonObject["rows"]!!.jsonPrimitive.int
            val columns = value.jsonObject["columns"]!!.jsonPrimitive.int
            val tileSizeX = value.jsonObject["tileSizeX"]!!.jsonPrimitive.int
            val tileSizeY = value.jsonObject["tileSizeY"]!!.jsonPrimitive.int
            val lawnUpperLeftCornerX = value.jsonObject["lawnUpperLeftCornerX"]!!.jsonPrimitive.int
            val lawnUpperLeftCornerY = value.jsonObject["lawnUpperLeftCornerY"]!!.jsonPrimitive.int
            val plantSize = value.jsonObject["plantSize"]!!.jsonPrimitive.float
            val zombieSize = value.jsonObject["zombieSize"]!!.jsonPrimitive.float


            val tileKeys = value.jsonObject["tileKeys"]!!.jsonObject
            val tileSetArray = value.jsonObject["tileSet"]!!.jsonArray
            val tileSet: List<List<TileType>> =
                tileSetArray.map { row -> row.jsonPrimitive.content.map { char ->
                    TileRegistry.get((tileKeys[char.toString()] as JsonPrimitive).content)
                }}

            lawns[id] = LawnType(
                id, resourcesVfs[asset], sunSprite, rows, columns,
                tileSizeX to tileSizeY,
                lawnUpperLeftCornerX to lawnUpperLeftCornerY,
                plantSize, zombieSize, tileSet
            )
        }
    }

    fun get(id: String): LawnType =
        lawns[id] ?: error("Unknown lawn type: $id")
}
