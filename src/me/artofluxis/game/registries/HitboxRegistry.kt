package me.artofluxis.game.registries

import me.artofluxis.game.dataFolder
import kotlinx.serialization.json.*
import me.artofluxis.game.game.hitbox.*
import java.io.*

data object HitboxRegistry : Registry {
    val hitboxes = HashMap<String, Hitbox>()

    override suspend fun load() {
        val text = File(dataFolder, "hitboxes.json").readText()
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
