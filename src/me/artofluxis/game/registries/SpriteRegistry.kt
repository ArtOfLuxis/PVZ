package me.artofluxis.game.registries

import me.artofluxis.game.dataFolder
import kotlinx.serialization.json.*
import me.artofluxis.game.loadBitmap
import java.io.*

data object SpriteRegistry : Registry {
    val sprites = HashMap<String, String>()

    override suspend fun load() {
        val text = File(dataFolder, "sprites.json").readText()
        val json = Json.parseToJsonElement(text).jsonArray

        for (value in json) {
            val id = value.jsonObject["id"]!!.jsonPrimitive.content
            val asset = value.jsonObject["asset"]!!.jsonPrimitive.content
            loadBitmap(asset)

            sprites[id] = asset
        }
    }

    fun get(id: String): String =
        sprites[id] ?: error("Unknown sprite id: $id")
}
