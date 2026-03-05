package registries

import korlibs.io.file.*
import korlibs.io.file.std.*
import kotlinx.serialization.json.*

object GlobalRegistry {
    lateinit var iconAsset: String

    suspend fun load() {
        val text = resourcesVfs["data/global.json"].readString()
        val json = Json.parseToJsonElement(text).jsonObject

        iconAsset = json["iconAsset"]!!.jsonPrimitive.content
    }
}
