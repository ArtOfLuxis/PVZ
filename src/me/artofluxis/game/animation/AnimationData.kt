package me.artofluxis.game.animation

import korlibs.image.bitmap.*
import korlibs.math.geom.slice.*
import kotlinx.serialization.json.*

class AnimationData(
    val frames: List<RectSlice<Bitmap>>,
    val frameDuration: Double,
    val loop: Boolean,
    val extraFrameData: Map<Int, String>
) {
    companion object {
        fun fromSpriteSheetJson(
            sheet: Bitmap,
            jsonText: String,
            loop: Boolean,
            fps: Double,
            extraFrameData: Map<Int, String>,
            crop: (src: Bitmap, x: Int, y: Int, w: Int, h: Int) -> RectSlice<Bitmap>
        ): AnimationData {
            val json = Json { ignoreUnknownKeys = true }
            val rootElement = json.parseToJsonElement(jsonText)
            val root = rootElement.jsonObject

            val framesElement = root["frames"]
                ?: throw IllegalArgumentException("Sprite-sheet JSON missing 'frames'")

            val frames = ArrayList<RectSlice<Bitmap>>()

            for ((_, frameEntry) in framesElement.jsonObject.entries) {
                val feObj = frameEntry.jsonObject
                val frameObj = feObj["frame"]?.jsonObject
                    ?: throw IllegalArgumentException("Frame entry missing 'frame' object")
                val x = frameObj["x"]!!.jsonPrimitive.int
                val y = frameObj["y"]!!.jsonPrimitive.int
                val w = frameObj["w"]!!.jsonPrimitive.int
                val h = frameObj["h"]!!.jsonPrimitive.int
                frames.add(crop(sheet, x, y, w, h))
            }

            return AnimationData(frames, 1.0 / fps, loop, extraFrameData)
        }
    }
}
