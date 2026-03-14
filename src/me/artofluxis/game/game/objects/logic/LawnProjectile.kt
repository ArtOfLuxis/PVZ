package me.artofluxis.game.game.objects.logic

import korlibs.korge.view.*
import me.artofluxis.game.*
import me.artofluxis.game.animation.*
import me.artofluxis.game.game.objects.*
import me.artofluxis.game.game.scenes.*
import me.artofluxis.game.game.types.*
import me.artofluxis.game.mod.trait.*

@Suppress("MemberVisibilityCanBePrivate", "CanBeParameter")
class LawnProjectile(
    override var pos: Position,
    override val row: Int,
    override var team: ObjectTeam?,
    override val scene: InGameScene,
    override var image: Image?,
    override val animationPlayer: AnimationPlayer,
    override val traits: HashSet<TraitInstance>,
    val type: ProjectileType,
    val parentShooter: LocationalLawnObject
): TickableLawnObject(), LocationalLawnObject {
    override val highlightFilter = parentShooter.highlightFilter

    override fun hitHitbox() = type.hitHitbox

    override fun toString(): String = "${this::class.simpleName}[${type.id}]"

    override fun scale() = 1.0
    override fun offset() = 0.0 to 0.0
}
