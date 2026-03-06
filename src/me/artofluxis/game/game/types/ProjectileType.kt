package me.artofluxis.game.game.types

import me.artofluxis.game.game.hitbox.*
import me.artofluxis.game.trait.*

data class ProjectileType(
    val id: String,
    val asset: String,
    var damage: Int,
    val hitHitbox: Hitbox,
    val traits: HashSet<Trait>
)
