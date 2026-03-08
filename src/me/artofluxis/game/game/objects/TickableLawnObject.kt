package me.artofluxis.game.game.objects

import kotlinx.coroutines.*
import me.artofluxis.game.*
import me.artofluxis.game.trait.*
import me.artofluxis.game.trait.events.general.*
import me.artofluxis.game.trait.requests.*

abstract class TickableLawnObject : LawnObject {
    protected open val traits = hashSetOf<TraitInstance>()
    private val tickTraitListeners = hashSetOf<TickTraitListener>()
    private val dataRequestListeners = hashSetOf<DataRequestListener>()
    private var shouldTick = true

    fun destroyTraits() {
        shouldTick = false

        val snapshot = getTraitsSnapshot()
        for (t in snapshot) {
            t.destroy()
        }

        tickTraitListeners.clear()
        dataRequestListeners.clear()
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

        traits.add(trait)

        if (trait is TickTraitListener)
            tickTraitListeners.add(trait)

        if (trait is DataRequestListener)
            dataRequestListeners.add(trait)
    }

    fun removeTrait(trait: TraitInstance) {
        traits.remove(trait)

        if (trait is TickTraitListener)
            tickTraitListeners.remove(trait)

        if (trait is DataRequestListener)
            dataRequestListeners.remove(trait)
    }

    fun getTraitsSnapshot() = traits.toList()

    fun <T> requestData(name: String): List<T> {
        val results = mutableListOf<T>()

        dataRequestListeners.forEach {
            @Suppress("UNCHECKED_CAST")
            val data = it.onDataRequest(name) as? T
            if (data != null) results.add(data)
        }

        return results
    }
}
