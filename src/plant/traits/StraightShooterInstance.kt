package plant.traits

import Position
import game.objects.logic.*
import trait.*
import trait.events.*

class StraightShooterInstance(
    parent: LawnPlant,
    override val trait: StraightShooterTrait
) : TraitInstance(parent, trait),
    TickTraitListener
{

    private var attackTimer = 0.0
    private var projectileTimer = 0.0
    private var projectilesLeft = 0

    override fun tick(deltaTime: Double) {
        if (projectilesLeft <= 0) {
            attackTimer += deltaTime

            if (attackTimer >= trait.interval) {
                attackTimer = -trait.additionalInterval
                projectilesLeft = trait.projectileAmount
                projectileTimer = 0.0
            }
            return
        }

        projectileTimer += deltaTime

        if (projectileTimer >= trait.projectileInterval) {
            projectileTimer = 0.0
            shoot()
            projectilesLeft--
        }
    }

    private fun shoot() {
        for (rowOffset in trait.attackRows) {
            spawnProjectile(rowOffset)
        }
    }

    private fun spawnProjectile(rowOffset: Int) {
        val tileSize = parent.scene.lawnType.tileSize
        val projectile = LawnProjectile(
            Position(
                parent.pos.x +  trait.projectilePositionOffset.x              * tileSize.first,
                parent.pos.y + (trait.projectilePositionOffset.y + rowOffset) * tileSize.second
            ),
            parent.row + rowOffset, trait.projectile.hitHitbox, parent.team, parent.scene,
            null, hashSetOf(), trait.projectile, parent
        )
        trait.projectile.traits.forEach { projectile.traits.add(it.createInstance(projectile)) }

        parent.scene.putProjectile(projectile)
    }
}
