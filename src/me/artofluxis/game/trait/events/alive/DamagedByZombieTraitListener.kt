package me.artofluxis.game.trait.events.alive

import me.artofluxis.game.game.objects.logic.LawnZombie

interface DamagedByZombieTraitListener {
    fun damagedByZombie(zombie: LawnZombie)
}
