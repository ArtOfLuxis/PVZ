package game.objects.logic

import HighlightFilter
import Position
import game.hitbox.*
import game.objects.LawnObject
import game.objects.ObjectTeam
import game.objects.TickableLawnObject
import game.scenes.*
import game.types.*
import korlibs.korge.view.*
import trait.*
import trait.events.alive.*

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
    override val highlightFilter = HighlightFilter(mutableListOf())
    override fun asset() = type.asset

    fun projectileHitObject(obj: LawnObject) {
        traits.forEach {
            if (it is ProjectileHitObjectTraitListener) it.projectileHitObject(obj)
        }
    }
}
