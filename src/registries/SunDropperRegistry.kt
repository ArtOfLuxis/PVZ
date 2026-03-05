package registries

import korlibs.io.file.std.*
import kotlinx.serialization.json.*
import lawn.*
import plant.*
import trait.*

object SunDropperRegistry {
    val sunDroppers = HashMap<String, SunDropper>()

    suspend fun load() {
        val text = resourcesVfs["data/sundroppers.json"].readString()
        val json = Json.parseToJsonElement(text).jsonArray

        for (value in json) {
            val id = value.jsonObject["id"]!!.jsonPrimitive.content
            val initialDelay = value.jsonObject["initialDelay"]!!.jsonPrimitive.double
            val delay = value.jsonObject["delay"]!!.jsonPrimitive.double
            val sunValue = value.jsonObject["sunValue"]!!.jsonPrimitive.int

            sunDroppers[id] = SunDropper(
                id, initialDelay, delay, sunValue
            )
        }
    }

    fun get(id: String): SunDropper =
        sunDroppers[id] ?: error("Unknown sun dropper id: $id")
}
