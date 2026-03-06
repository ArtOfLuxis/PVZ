package registries

import game.hitbox.*
import korlibs.io.file.std.*
import kotlinx.serialization.json.*

data object HitboxRegistry : Registry {
    val hitboxes = HashMap<String, Hitbox>()

    override suspend fun load() {
        val text = resourcesVfs["data/hitboxes.json"].readString()
        val json = Json.parseToJsonElement(text).jsonArray

        for (value in json) {
            val id = value.jsonObject["id"]!!.jsonPrimitive.content
            val width = value.jsonObject["width"]!!.jsonPrimitive.double
            val height = value.jsonObject["height"]!!.jsonPrimitive.double
            val xOffset = value.jsonObject["xOffset"]!!.jsonPrimitive.double
            val yOffset = value.jsonObject["yOffset"]!!.jsonPrimitive.double
            val center = HitboxCenter.valueOf(value.jsonObject["center"]!!.jsonPrimitive.content)
            val affectOtherRows = value.jsonObject["affectOtherRows"]!!.jsonPrimitive.boolean

            hitboxes[id] = Hitbox(id, width, height, xOffset, yOffset, center, affectOtherRows)
        }
    }

    fun get(id: String): Hitbox =
        hitboxes[id] ?: error("Unknown hitbox id: $id")
}
