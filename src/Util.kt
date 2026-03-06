import korlibs.image.bitmap.*
import korlibs.image.format.*
import korlibs.io.file.std.*

val bitmapCache = HashMap<String, Bitmap>()

fun getBitmap(assetPath: String): Bitmap = bitmapCache[assetPath] ?: error("Missing preloaded bitmap for $assetPath")
suspend fun loadBitmap(assetPath: String) {
    if (assetPath !in bitmapCache) bitmapCache[assetPath] = resourcesVfs[assetPath].readBitmap()
}

data class Position(val x: Double, val y: Double)
