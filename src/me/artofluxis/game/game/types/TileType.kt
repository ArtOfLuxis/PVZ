package me.artofluxis.game.game.types

import me.artofluxis.game.animation.*
import me.artofluxis.game.mod.trait.Trait

data class TileType(
    val id: String,
    val animationPack: AnimationPack?,
    val traits: HashSet<Trait>
) {
    override fun toString() = "${this::class.simpleName}[$id]"
}
