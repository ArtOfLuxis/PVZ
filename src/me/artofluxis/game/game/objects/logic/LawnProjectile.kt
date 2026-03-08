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

@Suppress("MemberVisibilityCanBePrivate", "CanBeParameter")
class LawnProjectile(
    override var pos: Position,
    override val row: Int,
    override var team: ObjectTeam,
    override val scene: InGameScene,
    override var image: Image?,
    override val traits: HashSet<TraitInstance>,
    val type: ProjectileType,
    val parentShooter: LawnObject
): TickableLawnObject() {
    override val highlightFilter = parentShooter.highlightFilter
    override fun asset() = type.asset

    fun projectileHitObject(obj: LawnObject, damage: Double) {
        traits.forEach {
            if (it is ProjectileHitObjectTraitListener) it.projectileHitObject(obj, damage)
        }
    }

    override fun toString(): String = "${this::class.simpleName}[${type.id}]"

    override fun offset(): Pair<Double, Double> = 0.0 to 0.0
}
