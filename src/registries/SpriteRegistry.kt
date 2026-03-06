package registries

import korlibs.io.file.*
import korlibs.io.file.std.*
import kotlinx.serialization.json.*
import lawn.*
import loadBitmap
import plant.*
import trait.*

data object SpriteRegistry : Registry {
    val sprites = HashMap<String, String>()

    override suspend fun load() {
        val text = resourcesVfs["data/sprites.json"].readString()
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
