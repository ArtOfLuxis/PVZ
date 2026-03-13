package me.artofluxis.game.animation

import me.artofluxis.game.game.objects.*

class AnimationPlayer(
    val pack: AnimationPack,
) {
    var parent: LawnObject? = null

    private val defaultAnimationEntry = pack.animations.entries.first()

    private var current: AnimationData = defaultAnimationEntry.value
    private var currentName: String = defaultAnimationEntry.key

    private var timer = 0.0
    private var frameIndex = 0

    private var extraDataCallback: (Int, String) -> Unit = { _, _ -> }

    init {
        play(currentName, true, extraDataCallback)
    }

    fun play(name: String, restart: Boolean = false, extraDataCallback: (Int, String) -> Unit) {
        if (!restart && name == currentName) return

        currentName = name
        current = pack.get(name)

        this.extraDataCallback = extraDataCallback

        frameIndex = 0
        timer = 0.0

        if (parent != null)
            updateParent(current.extraFrameData[frameIndex])
    }

    fun update(delta: Double) {
        timer += delta

        while (timer >= current.frameDuration) {
            timer -= current.frameDuration
            frameIndex++

            if (frameIndex >= current.frames.size) {
                if (current.loop)
                    frameIndex = 0
                else
                    play(defaultAnimationEntry.key, false) { _, _ -> }
            }

            updateParent(current.extraFrameData[frameIndex])
        }
    }

    fun frame() = current.frames[frameIndex]

    fun updateParent(extraData: String?) {
        if (extraData != null) extraDataCallback(frameIndex, extraData)

        parent!!.updateImage(frame())
    }
}
