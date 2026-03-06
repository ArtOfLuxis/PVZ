package me.artofluxis.game.game.objects.logic

import me.artofluxis.game.Position
import me.artofluxis.game.game.objects.LawnObject
import me.artofluxis.game.game.objects.ObjectTeam
import me.artofluxis.game.game.objects.TickableLawnObject
import korlibs.korge.view.*
import me.artofluxis.game.game.hitbox.*
import me.artofluxis.game.game.scenes.*
import me.artofluxis.game.game.types.*
import me.artofluxis.game.trait.*
import me.artofluxis.game.trait.events.alive.*

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
    override val highlightFilter = parentShooter.highlightFilter
    override fun asset() = type.asset

    fun projectileHitObject(obj: LawnObject) {
        traits.forEach {
            if (it is ProjectileHitObjectTraitListener) it.projectileHitObject(obj)
        }
    }
}
