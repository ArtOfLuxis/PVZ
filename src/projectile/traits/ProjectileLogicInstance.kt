package projectile.traits

import Position
import game.objects.*
import game.objects.logic.*
import korlibs.math.geom.*
import trait.*
import trait.events.*

class ProjectileLogicInstance(
    override val parent: LawnProjectile,
    override val trait: ProjectileLogicTrait
) : TraitInstance(parent, trait), TickTraitListener {

    override fun tick(deltaTime: Double) {
        val lawnType = parent.scene.lawnType
        val tileSize = lawnType.tileSize
        if (parent.pos.x >= lawnType.lawnUpperLeftCorner.first + tileSize.first * (lawnType.columns + 5)) {
            parent.scene.removeObject(parent)
            return
        }

        parent.pos = Position(
            parent.pos.x + parent.scene.lawnType.tileSize.first * deltaTime * trait.speed,
            parent.pos.y
        )
        parent.image!!.pos = Vector2D(parent.pos.x, parent.pos.y)

        val hitObject = parent.findIntersectingObjects { lawnObject, _ ->
            lawnObject.team != parent.team
                && lawnObject.row == parent.row
                && lawnObject is AliveLawnObject
        }.firstOrNull() as? AliveLawnObject

        if (hitObject != null) {
            hitObject.hitByProjectile(parent)
            parent.scene.removeObject(parent)
        }
    }
}
