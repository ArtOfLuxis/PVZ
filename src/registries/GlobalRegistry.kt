package registries

import korlibs.io.file.*
import korlibs.io.file.std.*
import kotlinx.serialization.json.*
import loadBitmap

data object GlobalRegistry : Registry {
    var iconAsset: String? = null
        private set
    var showDebugHitboxes: Boolean? = null
        private set

    override suspend fun load() {
        val text = resourcesVfs["data/global.json"].readString()
        val json = Json.parseToJsonElement(text).jsonObject

        iconAsset = json["iconAsset"]!!.jsonPrimitive.content
        loadBitmap(iconAsset!!)
        showDebugHitboxes = json["showDebugHitboxes"]!!.jsonPrimitive.boolean
    }
}
