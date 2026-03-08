package me.artofluxis.game.trait

import me.artofluxis.game.game.objects.TickableLawnObject

abstract class TraitInstance(
    open val parent: TickableLawnObject,
    open val trait: Trait
) {
    /** Supposed to be called to clean-up coroutines etc. etc. on trait destruction */
    open fun destroy() {}
}
