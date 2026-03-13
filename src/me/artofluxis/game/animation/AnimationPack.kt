package me.artofluxis.game.animation

class AnimationPack(
    val animations: LinkedHashMap<String, AnimationData>
) {
    fun get(name: String): AnimationData =
        animations[name] ?: error("Animation '$name' not found")
}
