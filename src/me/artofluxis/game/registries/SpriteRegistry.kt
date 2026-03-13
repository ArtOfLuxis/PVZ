package me.artofluxis.game.registries

import kotlinx.serialization.json.*
import me.artofluxis.game.*
import me.artofluxis.game.animation.*
import java.io.*

data object SpriteRegistry : Registry {
    val sprites = HashMap<String, AnimationPack>()

    override suspend fun load() {
        val text = File(dataFolder, "sprites.json").readText()
        val json = Json.parseToJsonElement(text).jsonArray

        for (value in json) {
            val id = value.jsonObject["id"]!!.jsonPrimitive.content

            val animationPackPath = value.jsonObject["animationPack"]!!.jsonPrimitive.content
            val animationPack = animationPackFromPath(animationPackPath)

            sprites[id] = animationPack
        }
    }

    fun get(id: String): AnimationPack =
        sprites[id] ?: error("Unknown sprite id: $id")
}
