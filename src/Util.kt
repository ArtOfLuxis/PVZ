import game.scenes.*
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
import kotlin.collections.HashMap
import kotlin.collections.MutableList
import kotlin.collections.contains
import kotlin.collections.forEach
import kotlin.collections.set
import kotlin.math.*

val bitmapCache = HashMap<String, Bitmap>()

fun getBitmap(assetPath: String): Bitmap = bitmapCache[assetPath] ?: error("Missing preloaded bitmap for $assetPath")
suspend fun loadBitmap(assetPath: String) {
    if (assetPath !in bitmapCache) bitmapCache[assetPath] = resourcesVfs[assetPath].readBitmap()
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

suspend fun handleException(e: Exception, stage: Stage) {
    stage.gameWindow.setSize(1200, 600)
    stage.gameWindow.title = "Encountered Exception"

    val sceneContainer = stage.sceneContainer()
    sceneContainer.changeTo { ExceptionScene(e) }

    e.printStackTrace()
}
