package tile

import korlibs.io.file.*
import trait.*

data class TileType(
    val id: String,
    val asset: VfsFile?,
    val traits: HashSet<Trait>
)
