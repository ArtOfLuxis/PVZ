package me.artofluxis.game.trait.events.alive

import me.artofluxis.game.game.objects.logic.*
import me.artofluxis.game.trait.*

interface DamagedByZombieTraitListener: TraitTrigger {
    fun damagedByZombie(zombie: LawnZombie, damage: Double)
}
