package trait.events.alive

import game.objects.logic.*

interface DamagedByZombieTraitListener {
    fun damagedByZombie(zombie: LawnZombie)
}
