package registries

import game.hitbox.*
import korlibs.io.file.*
import korlibs.io.file.std.*
import kotlinx.serialization.json.*
import lawn.*
import plant.*
import trait.*

object HitboxRegistry {
    val hitboxes = HashMap<String, Hitbox>()

    suspend fun load() {
        val text = resourcesVfs["data/hitboxes.json"].readString()
        val json = Json.parseToJsonElement(text).jsonArray

        for (value in json) {
            val id = value.jsonObject["id"]!!.jsonPrimitive.content
            val width = value.jsonObject["width"]!!.jsonPrimitive.double
            val height = value.jsonObject["height"]!!.jsonPrimitive.double
            val xOffset = value.jsonObject["xOffset"]!!.jsonPrimitive.double
            val yOffset = value.jsonObject["yOffset"]!!.jsonPrimitive.double
            val center = HitboxCenter.valueOf(value.jsonObject["center"]!!.jsonPrimitive.content)

            hitboxes[id] = Hitbox(id, width, height, xOffset, yOffset, center)
        }
    }

    fun get(id: String): Hitbox =
        hitboxes[id] ?: error("Unknown hitbox id: $id")
}
