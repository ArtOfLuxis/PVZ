package me.artofluxis.game.game.types

import me.artofluxis.game.animation.*
import me.artofluxis.game.game.hitbox.*
import me.artofluxis.game.trait.*

data class ProjectileType(
    val id: String,
    val animationPack: AnimationPack,
    val hitHitbox: Hitbox,
    val traits: HashSet<Trait>
) {
    override fun toString() = "${this::class.simpleName}[$id]"
}
