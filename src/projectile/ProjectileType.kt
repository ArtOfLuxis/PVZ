package projectile

import game.hitbox.*
import korlibs.io.file.*
import trait.*

data class ProjectileType(
    val id: String,
    val asset: String,
    val damage: Int,
    val hitHitbox: Hitbox,
    val traits: HashSet<Trait>
)
