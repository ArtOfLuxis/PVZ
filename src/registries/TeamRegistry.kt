package registries

import game.*
import korlibs.io.file.*
import korlibs.io.file.std.*
import kotlinx.serialization.json.*

object TeamRegistry {
    val teams = HashMap<String, ObjectTeam>()

    suspend fun load() {
        val text = resourcesVfs["data/teams.json"].readString()
        val json = Json.parseToJsonElement(text).jsonArray

        for (value in json) {
            val id = value.jsonObject["id"]!!.jsonPrimitive.content

            teams[id] = ObjectTeam(id)
        }
    }

    fun get(id: String): ObjectTeam =
        teams[id] ?: error("Unknown team id: $id")
}
