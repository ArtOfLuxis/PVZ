package zombie.traits

import game.objects.logic.*
import trait.*
import trait.events.*

class CommonZombieLogicInstance(
    override val parent: LawnZombie,
    override val trait: CommonZombieLogicTrait
) : TraitInstance(parent, trait),
    HitByProjectileTraitListener,
    TickTraitListener
{
    override fun tick(deltaTime: Double) {
        TODO("Not yet implemented")
    }

    override fun hitByProjectile(projectile: LawnProjectile) {
        TODO("Not yet implemented")
    }
}
