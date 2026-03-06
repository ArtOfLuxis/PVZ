package me.artofluxis.game.registries

import me.artofluxis.game.dataFolder
import kotlinx.serialization.json.*
import me.artofluxis.game.game.objects.*
import java.io.*

data object TeamRegistry : Registry {
    val teams = HashMap<String, ObjectTeam>()

        override suspend fun load() {
            val text = File(dataFolder, "teams.json").readText()
        val json = Json.parseToJsonElement(text).jsonArray

        for (value in json) {
            val id = value.jsonObject["id"]!!.jsonPrimitive.content

            teams[id] = ObjectTeam(id)
        }
    }

    fun get(id: String): ObjectTeam =
        teams[id] ?: error("Unknown team id: $id")
}
