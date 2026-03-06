package game.types

import game.hitbox.*
import trait.*

data class ProjectileType(
    val id: String,
    val asset: String,
    val damage: Int,
    val hitHitbox: Hitbox,
    val traits: HashSet<Trait>
)
