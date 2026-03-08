package me.artofluxis.game.game.objects

import kotlinx.coroutines.*
import me.artofluxis.game.*
import me.artofluxis.game.trait.*
import me.artofluxis.game.trait.events.general.*

abstract class TickableLawnObject : LawnObject {
    protected open val traits = hashSetOf<TraitInstance>()
    private val tickTraitListeners = hashSetOf<TickTraitListener>()
    private var shouldTick = true

    fun destroyTraits() {
        shouldTick = false

        val snapshot = getTraitsSnapshot()
        for (t in snapshot) {
            t.destroy()
        }

        tickTraitListeners.clear()
        traits.clear()
    }

    fun tick(deltaTime: Double) {
        val snapshot = tickTraitListeners.toList()
        for (listener in snapshot) {
            try {
                if (!shouldTick) return

                listener.tick(deltaTime)
            } catch (e: Throwable) {
                scene.launch { handleException(e, "Error during trait tick") }
            }
        }
    }

    fun addTrait(trait: TraitInstance) {
        /*
        for (listenerClass in TraitTrigger.oncePerObjectTriggers) {
            if (listenerClass.java.isInstance(trait)) {

                if (traits.any { listenerClass.java.isInstance(it) }) {
                    val scene = this.scene
                    scene.launch {
                        handleException(IllegalStateException(
                            "Only one trait implementing ${listenerClass.simpleName} per object is allowed, but ${this@TickableLawnObject} tried to implement it a second time"
                        ), "Encountered Exception when adding a trait")
                    }
                }
            }
        }
        */

        traits.add(trait)

        if (trait is TickTraitListener)
            tickTraitListeners.add(trait)
    }

    fun removeTrait(trait: TraitInstance) {
        traits.remove(trait)

        if (trait is TickTraitListener)
            tickTraitListeners.remove(trait)
    }

    fun getTraitsSnapshot() = traits.toList()
}
