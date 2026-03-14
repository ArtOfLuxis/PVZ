package me.artofluxis.game.registries

import korlibs.image.bitmap.*
import korlibs.io.file.std.*
import kotlinx.serialization.json.*
import me.artofluxis.game.*
import me.artofluxis.game.animation.*
import me.artofluxis.game.game.types.*
import me.artofluxis.game.mod.trait.Trait
import me.artofluxis.game.mod.trait.TraitType
import java.io.*

data object TileRegistry : Registry {
    val tiles = HashMap<String, TileType>()

    override suspend fun load() {
        val text = File(dataFolder, "tiles.json").readText()
        val json = Json.parseToJsonElement(text).jsonArray

        for (value in json) {
            val id = value.jsonObject["id"]!!.jsonPrimitive.content

            val animationPack: AnimationPack?
            val animationPackPath = value.jsonObject["animationPack"]!!.jsonPrimitive.contentOrNull
            animationPack =
                if (animationPackPath == null) null
                else animationPackFromPath(animationPackPath)

            val traits = hashSetOf<Trait>()
            value.jsonObject["traits"]!!.jsonArray.forEach { obj ->
                val traitID = obj.jsonObject["id"]!!.jsonPrimitive.content
                val trait = Trait.from(traitID, obj.jsonObject)

                if (trait.traitType != TraitType.TILE && trait.traitType != TraitType.GENERIC)
                    error("Non-tile trait in a tile definition $obj")
                traits.add(trait)
            }

            tiles[id] = TileType(id, animationPack, traits)
        }
    }

    fun get(id: String): TileType =
        tiles[id] ?: error("Unknown tile id: $id")
}
