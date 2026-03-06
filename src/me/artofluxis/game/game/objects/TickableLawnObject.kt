package me.artofluxis.game.game.objects

import me.artofluxis.game.trait.*
import me.artofluxis.game.trait.events.alive.*

interface TickableLawnObject : LawnObject {
    val traits: HashSet<TraitInstance>

    fun tick(deltaTime: Double) {
        traits.forEach { if (it is TickTraitListener) it.tick(deltaTime) }
    }
}
