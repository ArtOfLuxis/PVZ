package me.artofluxis.game.trait.requests

import me.artofluxis.game.trait.*

interface DataRequestListener: TraitTrigger {
    fun onDataRequest(name: String): Any?
}
