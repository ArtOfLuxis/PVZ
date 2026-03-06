package registries

import game.types.*
import korlibs.io.file.std.*
import kotlinx.serialization.json.*

data object SunDropperRegistry : Registry {
    val sunDroppers = HashMap<String, SunDropperType>()

    override suspend fun load() {
        val text = resourcesVfs["data/sundroppers.json"].readString()
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
