package trait.events.alive

import game.objects.*

interface ZombieDeathListener {
    fun onZombieDeath(damager: LawnObject)
}
