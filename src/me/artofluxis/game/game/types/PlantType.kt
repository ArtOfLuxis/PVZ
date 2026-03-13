package me.artofluxis.game.game.types

import me.artofluxis.game.animation.*
import me.artofluxis.game.game.hitbox.*
import me.artofluxis.game.trait.*

data class PlantType(
    val id: String,
    val name: String,
    val hitHitbox: Hitbox,
    val sunCost: Int,
    val refreshTime: Double,
    val scale: Double,
    val animationPack: AnimationPack,
    val packetAsset: String,
    val traits: HashSet<Trait>
) {
    override fun toString() = "${this::class.simpleName}[$id]"
}
