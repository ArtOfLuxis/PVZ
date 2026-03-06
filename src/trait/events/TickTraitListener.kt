package trait.events

import game.objects.logic.*

interface TickTraitListener {
    fun tick(deltaTime: Double)
}
