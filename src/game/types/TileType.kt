package game.types

import trait.*

data class TileType(
    val id: String,
    val asset: String?,
    val traits: HashSet<Trait>
)
