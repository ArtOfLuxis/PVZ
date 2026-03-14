package me.artofluxis.game.game.objects

import korlibs.image.bitmap.*
import korlibs.korge.view.*
import korlibs.korge.view.filter.*
import korlibs.math.geom.slice.*
import me.artofluxis.game.*
import me.artofluxis.game.animation.*
import me.artofluxis.game.game.hitbox.*
import me.artofluxis.game.game.scenes.*

interface LawnObject {
    val scene: InGameScene
    var image: Image?
    val animationPlayer: AnimationPlayer?
    val highlightFilter: HighlightFilter

    fun setNewImage(newImage: Image) {
        this.image = newImage
        this.image!!.filter = highlightFilter
    }

    fun updateImage(newBitmap: RectSlice<Bitmap>) {
        this.image!!.apply {
            bitmap = newBitmap
        }
    }
}
