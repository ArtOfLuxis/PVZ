package me.artofluxis.game.game.types

import me.artofluxis.game.game.hitbox.*
import me.artofluxis.game.trait.*

data class PlantType(
    val id: String,
    val name: String,
    var sunCost: Int,
    var refreshTime: Double,
    var toughness: Double,
    val hitHitbox: Hitbox,
    val spriteAsset: String,
    val packetAsset: String,
    val traits: HashSet<Trait>
)
