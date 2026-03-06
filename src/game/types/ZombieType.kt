package game.types

import game.hitbox.*
import trait.*

data class ZombieType(
    val id: String,
    val name: String,
    val toughness: Double,
    val hitHitbox: Hitbox,
    val asset: String,
    val traits: HashSet<Trait>
)
