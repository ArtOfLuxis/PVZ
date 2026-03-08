package me.artofluxis.game

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
import me.artofluxis.game.game.scenes.*
import kotlin.collections.HashMap
import kotlin.collections.MutableList
import kotlin.collections.contains
import kotlin.collections.forEach
import kotlin.collections.set

object BitmapLoader {
    val bitmapCache = HashMap<String, Bitmap>()

    fun getBitmap(assetPath: String): Bitmap = bitmapCache[assetPath]
        ?: error("Missing preloaded bitmap for $assetPath")

    suspend fun loadBitmap(assetPath: String) {
        if (assetPath !in bitmapCache) {
            bitmapCache[assetPath] =
                localVfs(resourcesFolder.absolutePath)[assetPath].readBitmap()
        }
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
    val x2 = x + w
    val y2 = y + h

    for (px in x..x2) {
        if (px in 0 until width) {
            if (y in 0 until height) this[px, y] = color
            if (y2 in 0 until height) this[px, y2] = color
        }
    }

    for (py in y..y2) {
        if (py in 0 until height) {
            if (x in 0 until width) this[x, py] = color
            if (x2 in 0 until width) this[x2, py] = color
        }
    }
}
