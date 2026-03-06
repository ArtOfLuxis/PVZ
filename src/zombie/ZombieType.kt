package zombie

import korlibs.io.file.*
import trait.*

data class ZombieType(
    val id: String,
    val name: String,
    val toughness: Double,
    val asset: String,
    val traits: HashSet<Trait>
)
