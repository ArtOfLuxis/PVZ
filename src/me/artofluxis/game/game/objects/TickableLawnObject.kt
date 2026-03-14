package me.artofluxis.game.game.objects

import kotlinx.coroutines.*
import me.artofluxis.game.*
import me.artofluxis.game.mod.trait.*

abstract class TickableLawnObject : LawnObject {
    protected open val traits: MutableSet<TraitInstance> = hashSetOf()
    var shouldTick = true

    fun destroyTraits() {
        shouldTick = false

        val snapshot = getTraitsSnapshot()
        for (t in snapshot) {
            t.destroy()
        }

        traits.clear()
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
    }

    fun removeTrait(trait: ObjectTraitInstance) {
        traits.remove(trait)
    }

    fun getTraitsSnapshot() = traits.toList()
}
