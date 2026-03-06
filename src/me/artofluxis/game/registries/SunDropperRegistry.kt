package me.artofluxis.game.registries

import me.artofluxis.game.dataFolder
import kotlinx.serialization.json.*
import me.artofluxis.game.game.types.*
import java.io.*

data object SunDropperRegistry : Registry {
    val sunDroppers = HashMap<String, SunDropperType>()

    override suspend fun load() {
        val text = File(dataFolder, "sundroppers.json").readText()
        val json = Json.parseToJsonElement(text).jsonArray

        for (value in json) {
            val id = value.jsonObject["id"]!!.jsonPrimitive.content
            val initialDelay = value.jsonObject["initialDelay"]!!.jsonPrimitive.double
            val delay = value.jsonObject["delay"]!!.jsonPrimitive.double
            val sunValue = value.jsonObject["sunValue"]!!.jsonPrimitive.int

            sunDroppers[id] = SunDropperType(
                id, initialDelay, delay, sunValue
            )
        }
    }

    fun get(id: String): SunDropperType =
        sunDroppers[id] ?: error("Unknown sun dropper id: $id")
}
