package plant

import korlibs.io.file.*
import trait.*

data class PlantType(
    val id: String,
    val name: String,
    val sunCost: Int,
    val refreshTime: Double,
    val toughness: Int,
    val spriteAsset: VfsFile,
    val packetAsset: VfsFile,
    val traits: HashSet<Trait>
)
