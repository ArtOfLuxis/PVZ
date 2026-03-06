package game.objects

import trait.*
import trait.events.*

interface TickableLawnObject : LawnObject {
    val traits: HashSet<TraitInstance>

    fun tick(deltaTime: Double) {
        traits.forEach { if (it is TickTraitListener) it.tick(deltaTime) }
    }
}
