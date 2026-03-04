package registries

import korlibs.io.file.*
import korlibs.io.file.std.*
import kotlinx.serialization.json.*
import lawn.*
import plant.*
import trait.*

object SpriteRegistry {
    val sprites = HashMap<String, VfsFile>()

    suspend fun load() {
        val text = resourcesVfs["data/sprites.json"].readString()
        val json = Json.parseToJsonElement(text).jsonArray

        for (value in json) {
            val id = value.jsonObject["id"]!!.jsonPrimitive.content
            val asset = "textures/" + value.jsonObject["asset"]!!.jsonPrimitive.content

            sprites[id] = resourcesVfs[asset]
        }
    }

    fun get(id: String): VfsFile =
        sprites[id] ?: error("Unknown sun dropper type: $id")
}
