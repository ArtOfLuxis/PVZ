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
            val asset = value.jsonObject["asset"]!!.jsonPrimitive.content
            val rows = value.jsonObject["rows"]!!.jsonPrimitive.content.toInt()
            val columns = value.jsonObject["columns"]!!.jsonPrimitive.content.toInt()
            val tileSizeX = value.jsonObject["tileSizeX"]!!.jsonPrimitive.content.toInt()
            val tileSizeY = value.jsonObject["tileSizeY"]!!.jsonPrimitive.content.toInt()
            val lawnUpperLeftCornerX = value.jsonObject["lawnUpperLeftCornerX"]!!.jsonPrimitive.content.toInt()
            val lawnUpperLeftCornerY = value.jsonObject["lawnUpperLeftCornerY"]!!.jsonPrimitive.content.toInt()
            val plantSize = value.jsonObject["plantSize"]!!.jsonPrimitive.content.toFloat()
            val zombieSize = value.jsonObject["zombieSize"]!!.jsonPrimitive.content.toFloat()


            val tileKeys = value.jsonObject["tileKeys"]!!.jsonObject
            val tileSetArray = value.jsonObject["tileSet"]!!.jsonArray
            val tileSet: List<List<TileType>> =
                tileSetArray.map { row -> row.jsonPrimitive.content.map { char ->
                    TileRegistry.get((tileKeys[char.toString()] as JsonPrimitive).content)
                }}

            lawns[id] = LawnType(
                id, resourcesVfs[asset], rows, columns,
                tileSizeX to tileSizeY,
                lawnUpperLeftCornerX to lawnUpperLeftCornerY,
                plantSize, zombieSize, tileSet
            )
        }
    }

    fun get(id: String): LawnType =
        lawns[id] ?: error("Unknown lawn type: $id")
}
