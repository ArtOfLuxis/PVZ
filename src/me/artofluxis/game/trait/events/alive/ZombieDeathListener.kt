package me.artofluxis.game.trait.events.alive

import me.artofluxis.game.game.objects.LawnObject

interface ZombieDeathListener {
    fun onZombieDeath(damager: LawnObject)
}
