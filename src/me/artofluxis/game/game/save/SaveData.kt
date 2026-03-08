package me.artofluxis.game.game.save

import kotlinx.serialization.*
import kotlinx.serialization.json.*
import me.artofluxis.game.*
import java.io.File

@Serializable
class SaveData(
    var selectedJARMod: String,
    var selectedDataMod: String,
    var selectedResourceMod: String
) {
    fun save() {
        val file = File(runDir, "data/save.json")
        file.parentFile.mkdirs()

        val json = Json { prettyPrint = true }
        file.writeText(json.encodeToString(this))
    }
}
