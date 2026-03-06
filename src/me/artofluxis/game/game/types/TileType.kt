package me.artofluxis.game.game.types

import me.artofluxis.game.trait.*

data class TileType(
    val id: String,
    val asset: String?,
    val traits: HashSet<Trait>
)
