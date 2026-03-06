package registries

import game.types.*
import korlibs.io.file.std.*
import kotlinx.serialization.json.*
import loadBitmap

data object LawnRegistry : Registry {
    val lawns = HashMap<String, LawnType>()

    override suspend fun load() {
        val text = resourcesVfs["data/lawns.json"].readString()
        val json = Json.parseToJsonElement(text).jsonArray

        for (value in json) {
            val id = value.jsonObject["id"]!!.jsonPrimitive.content
            val asset = value.jsonObject["asset"]!!.jsonPrimitive.content
            loadBitmap(asset)
            val sunSprite = SpriteRegistry.get(value.jsonObject["sunSprite"]!!.jsonPrimitive.content)
            val rows = value.jsonObject["rows"]!!.jsonPrimitive.int
            val columns = value.jsonObject["columns"]!!.jsonPrimitive.int
            val tileSize = value.jsonObject["tileSize"]!!.jsonArray.let {
                it[0].jsonPrimitive.int to it[1].jsonPrimitive.int
            }
            val lawnUpperLeftCorner = value.jsonObject["lawnUpperLeftCorner"]!!.jsonArray.let {
                it[0].jsonPrimitive.int to it[1].jsonPrimitive.int
            }
            val plantSize = value.jsonObject["plantSize"]!!.jsonPrimitive.float
            val zombieSize = value.jsonObject["zombieSize"]!!.jsonPrimitive.float


            val defaultTile = TileRegistry.get(value.jsonObject["defaultTile"]!!.jsonPrimitive.content)
            val tileKeys = value.jsonObject["tileKeys"]!!.jsonObject
            val tileSetArray = value.jsonObject["tileSet"]!!.jsonArray
            val tileSet: HashMap<Pair<Int, Int>, TileType> =
                HashMap<Pair<Int, Int>, TileType>().apply {
                    tileSetArray.mapIndexed { y, row ->
                        row.jsonPrimitive.content.forEachIndexed { x, char ->
                            val tileName = (tileKeys[char.toString()] as JsonPrimitive).contentOrNull
                            if (tileName != null) {
                                put(Pair(x, y), TileRegistry.get(tileName))
                            }
                        }
                    }
                }
            val fullTileSet: List<List<TileType>> =
                tileSetArray.map { row -> row.jsonPrimitive.content.map { char ->
                    val tileName = (tileKeys[char.toString()] as JsonPrimitive).contentOrNull
                    if (tileName == null) defaultTile else TileRegistry.get(tileName)
                }}

            lawns[id] = LawnType(
                id, asset, sunSprite, rows, columns,
                tileSize, lawnUpperLeftCorner,
                plantSize, zombieSize, defaultTile, tileSet,
                fullTileSet.size, fullTileSet.map { it.size }
            )
        }
    }

    fun get(id: String): LawnType =
        lawns[id] ?: error("Unknown lawn id: $id")
}
