package me.artofluxis.game.registries

import kotlinx.serialization.json.*
import me.artofluxis.game.*
import me.artofluxis.game.game.types.*
import java.io.*

data object LawnRegistry : Registry {
    val lawns = HashMap<String, LawnType>()

    override suspend fun load() {
        val text = File(dataFolder, "lawns.json").readText()
        val json = Json.parseToJsonElement(text).jsonArray

        for (value in json) {
            val id = value.jsonObject["id"]!!.jsonPrimitive.content

            val animationPackPath = value.jsonObject["animationPack"]!!.jsonPrimitive.content
            val animationPack = animationPackFromPath(animationPackPath)

            val rows = value.jsonObject["rows"]!!.jsonPrimitive.int
            val columns = value.jsonObject["columns"]!!.jsonPrimitive.int
            val tileSize = value.jsonObject["tileSize"]!!.jsonArray.let {
                it[0].jsonPrimitive.int to it[1].jsonPrimitive.int
            }
            val lawnUpperLeftCorner = value.jsonObject["lawnUpperLeftCorner"]!!.jsonArray.let {
                it[0].jsonPrimitive.int to it[1].jsonPrimitive.int
            }
            val teamSizes = HashMap(value.jsonObject["teamSizes"]!!.jsonObject.map {
                TeamRegistry.get(it.key) to it.value.jsonPrimitive.double
            }.toMap())
            val teamOffsets = HashMap(value.jsonObject["teamOffsets"]!!.jsonObject.map {
                TeamRegistry.get(it.key) to it.value.jsonArray.let { array ->
                    array[0].jsonPrimitive.double to array[1].jsonPrimitive.double
                }
            }.toMap())

            val tileKeys = value.jsonObject["tileKeys"]!!.jsonObject
            val tileSetArray = value.jsonObject["tileSet"]!!.jsonArray
            val tileSet: List<List<TileType>> =
                tileSetArray.map { row -> row.jsonPrimitive.content.map { char ->
                    val tileName = (tileKeys[char.toString()] as JsonPrimitive).content
                    TileRegistry.get(tileName)
                }}

            lawns[id] = LawnType(
                id, animationPack, rows, columns,
                tileSize, lawnUpperLeftCorner,
                teamSizes, teamOffsets, tileSet
            )
        }
    }

    fun get(id: String): LawnType =
        lawns[id] ?: error("Unknown lawn id: $id")
}
