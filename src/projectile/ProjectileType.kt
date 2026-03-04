package projectile

import korlibs.io.file.*
import trait.*

data class ProjectileType(
    val id: String,
    val damage: Int,
    val asset: VfsFile,
    val traits: HashSet<Trait>
) {
}
