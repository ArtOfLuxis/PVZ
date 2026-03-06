package trait.events

import game.objects.logic.*

interface DamagedByZombieTraitListener {
    fun damagedByZombie(zombie: LawnZombie)
}
