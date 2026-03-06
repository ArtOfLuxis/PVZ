package game.objects.logic

import Position
import game.hitbox.*
import game.objects.LawnObject
import game.objects.ObjectTeam
import game.objects.TickableLawnObject
import game.scenes.*
import korlibs.korge.view.*
import projectile.*
import trait.*

class LawnProjectile(
    override var pos: Position,
    override val row: Int,
    override val hitHitbox: Hitbox,
    override var team: ObjectTeam,
    override val scene: InGameScene,
    override var image: Image?,
    override val traits: HashSet<TraitInstance>,
    val type: ProjectileType,
    val parentShooter: LawnObject
): TickableLawnObject {
    override fun asset() = type.asset
}
