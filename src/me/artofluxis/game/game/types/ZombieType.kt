package me.artofluxis.game.game.types

import me.artofluxis.game.game.hitbox.*
import me.artofluxis.game.trait.*

data class ZombieType(
    val id: String,
    val name: String,
    var toughness: Double,
    val hitHitbox: Hitbox,
    val asset: String,
    val traits: HashSet<Trait>
)
