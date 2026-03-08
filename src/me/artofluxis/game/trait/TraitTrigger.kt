package me.artofluxis.game.trait

import me.artofluxis.game.trait.requests.*
import kotlin.reflect.*

interface TraitTrigger {
    companion object {
        val oncePerObjectTriggers = hashSetOf<KClass<out TraitTrigger>>(
            DataRequestListener::class
        )
    }
}
