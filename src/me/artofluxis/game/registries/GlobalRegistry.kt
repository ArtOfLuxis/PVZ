package me.artofluxis.game.registries

import me.artofluxis.game.dataFolder
import kotlinx.serialization.json.*
import me.artofluxis.game.loadBitmap
import me.artofluxis.game.resourcesFolder
import java.io.*

data object GlobalRegistry : Registry {
    var iconAsset: String? = null
        private set
    var showDebugHitboxes: Boolean? = null
        private set
    var debugHitboxUpdateInterval: Double? = null
        private set
    var playBackgroundAsset: String? = null
        private set
    var settingsBackgroundAsset: String? = null
        private set
    var windowSize: Pair<Int, Int>? = null
        private set

    override suspend fun load() {
        val text = File(dataFolder, "global.json").readText()
        val json = Json.parseToJsonElement(text).jsonObject

        iconAsset = resourcesFolder.absolutePath + "/" + json["iconAsset"]!!.jsonPrimitive.content
        showDebugHitboxes = json["showDebugHitboxes"]!!.jsonPrimitive.boolean
        debugHitboxUpdateInterval = json["debugHitboxUpdateInterval"]!!.jsonPrimitive.double
        windowSize = json["windowSize"]!!.jsonArray.let {
            it[0].jsonPrimitive.int to it[1].jsonPrimitive.int
        }
        playBackgroundAsset = json["playBackgroundAsset"]!!.jsonPrimitive.content
        loadBitmap(playBackgroundAsset!!)
        settingsBackgroundAsset = json["settingsBackgroundAsset"]!!.jsonPrimitive.content
        loadBitmap(settingsBackgroundAsset!!)
    }
}
