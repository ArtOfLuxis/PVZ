package me.artofluxis.game

import korlibs.datastructure.*
import korlibs.image.bitmap.*
import korlibs.image.color.*
import korlibs.image.format.*
import korlibs.io.file.std.*
import korlibs.korge.render.*
import korlibs.korge.scene.*
import korlibs.korge.view.*
import korlibs.korge.view.filter.*
import korlibs.math.geom.*
import korlibs.time.*
import kotlinx.serialization.json.*
import me.artofluxis.game.animation.*
import me.artofluxis.game.game.scenes.*
import java.io.File
import kotlin.collections.HashMap
import kotlin.collections.MutableList
import kotlin.collections.contains
import kotlin.collections.forEach
import kotlin.collections.set

object BitmapLoader {
    val bitmapCache = HashMap<String, Bitmap>()

    fun getBitmap(assetPath: String): Bitmap = bitmapCache[assetPath]
        ?: error("Missing preloaded bitmap for $assetPath")

    suspend fun loadBitmap(assetPath: String): Bitmap {
        if (assetPath !in bitmapCache) {
            bitmapCache[assetPath] =
                localVfs(resourcesFolder.absolutePath)[assetPath].readBitmap()
        }

        return bitmapCache[assetPath]!!
    }
}



data class Position(val x: Double, val y: Double)

class HighlightFilter(
    val colors: MutableList<RGBA>
) : Filter {

    private fun mergedColor(): RGBA {
        if (colors.isEmpty()) return Colors.WHITE

        var r = 0
        var g = 0
        var b = 0
        var a = 0

        colors.forEach { c ->
            r += c.r
            g += c.g
            b += c.b
            a += c.a
        }

        val n = colors.size
        return RGBA(
            r / n,
            g / n,
            b / n,
            a / n
        )
    }

    override fun render(
        ctx: RenderContext,
        matrix: Matrix,
        texture: Texture,
        texWidth: Int,
        texHeight: Int,
        renderColorMul: RGBA,
        blendMode: BlendMode,
        filterScale: Double
    ) {
        val base = mergedColor()

        val colorToUse = RGBA(
            (base.r * renderColorMul.r) / 255,
            (base.g * renderColorMul.g) / 255,
            (base.b * renderColorMul.b) / 255,
            (base.a * renderColorMul.a) / 255
        )

        ctx.useBatcher {
            it.drawQuad(
                texture,
                x = 0f,
                y = 0f,
                width = texWidth.toFloat(),
                height = texHeight.toFloat(),
                m = matrix,
                colorMul = colorToUse,
                blendMode = blendMode
            )
        }
    }
}

class Timer(
    val start: Double,
    val time: Double
) {
    fun isExpired(): Boolean = now() >= start + time

    fun remaining(): Double = (start + time - now()).coerceAtLeast(0.0)

    companion object {
        fun now(): Double = DateTime.nowUnixMillis() / 1000.0

        fun start(time: Double) = Timer(now(), time)

        val INFINITE = Timer(0.0, Double.POSITIVE_INFINITY)
    }
}

suspend fun handleException(e: Throwable, title: String) {
    val stage = originalSceneContainer.stage!!

    stage.removeChildren()
    stage.gameWindow.setSize(1800, 900)
    stage.gameWindow.title = title

    val sceneContainer = stage.sceneContainer()
    sceneContainer.changeTo { ExceptionScene(e, title) }

    e.printStackTrace()
    saveData.save()
}

fun Bitmap32.drawRectOutline(x: Int, y: Int, w: Int, h: Int, color: RGBA) {
    val x0 = x.coerceAtLeast(0)
    val y0 = y.coerceAtLeast(0)
    val x1 = (x + w - 1).coerceAtMost(width - 1)
    val y1 = (y + h - 1).coerceAtMost(height - 1)

    if (x0 > x1 || y0 > y1) return

    for (px in x0..x1) {
        this[px, y0] = color
        if (y0 != y1) this[px, y1] = color
    }

    if (y1 - y0 >= 2) {
        for (py in (y0 + 1) until y1) {
            this[x0, py] = color
            if (x0 != x1) this[x1, py] = color
        }
    }
}

suspend fun animationPackFromPath(
    animationPackPath: String
): AnimationPack {
    val animationPackFolder = File("$resourcesFolder/$animationPackPath")

    if (!animationPackFolder.exists()) {
        val assetFile = File("$resourcesFolder/$animationPackPath.png")
        if (assetFile.exists()) {
            val bmp = BitmapLoader.loadBitmap("$animationPackPath.png")
            val single = AnimationData(listOf(bmp.slice()), 999.0, true, hashMapOf())
            return AnimationPack(linkedMapOf("normal" to single))
        }
        error("Animation pack folder not found: $animationPackPath")
    }

    // find animations.json
    val animationsJsonFile = File(animationPackFolder, "animations.json")
    require(animationsJsonFile.exists()) { "Missing animations.json in $animationPackPath" }

    val animationsRoot = Json.parseToJsonElement(animationsJsonFile.readText()).jsonObject
    val animationsObj = animationsRoot["animations"]?.jsonObject
        ?: error("animations.json missing 'animations' object in $animationPackPath")

    val animations = LinkedHashMap<String, AnimationData>()

    for ((animName, animJe) in animationsObj) {
        val animObj = animJe.jsonObject

        val loop = animObj["loop"]!!.jsonPrimitive.boolean

        val fps = animObj["fps"]!!.jsonPrimitive.double

        val extraFrameData: HashMap<Int, String> = HashMap(animObj["extraFrameData"]!!.jsonObject
            .mapNotNull { (k, v) ->
                k.toIntOrNull()?.let { it to v.jsonPrimitive.content }
            }.toMap())

        val sheetPath = "$animationPackPath/$animName/${animName}.png"
        println(sheetPath)
        require(File(resourcesFolder, sheetPath).exists()) { "No sprite sheet found for animation $animName" }

        val jsonFile = File(resourcesFolder, "$animationPackPath/$animName/${animName}.json")
        require(jsonFile.exists()) { "No json data found for animation $animName" }

        val sheetBitmap = BitmapLoader.loadBitmap(sheetPath)
        val jsonText = jsonFile.readText()

        val animData = AnimationData.fromSpriteSheetJson(
            sheet = sheetBitmap,
            jsonText = jsonText,
            loop = loop,
            fps = fps,
            extraFrameData = extraFrameData
        ) { src, x, y, w, h ->
            src.sliceWithSize(x, y, w, h)
        }

        animations[animName] = animData
    }

    return AnimationPack(animations)
}
