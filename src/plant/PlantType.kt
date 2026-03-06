package plant

import korlibs.io.file.*
import trait.*

data class PlantType(
    val id: String,
    val name: String,
    val sunCost: Int,
    val refreshTime: Double,
    val toughness: Double,
    val spriteAsset: String,
    val packetAsset: String,
    val traits: HashSet<Trait>
)
