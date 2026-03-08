package me.artofluxis.game.trait.events.tile

import me.artofluxis.game.game.objects.LawnObject
import me.artofluxis.game.trait.*

interface ExitedTileTraitListener: TraitTrigger {
    fun exited(obj: LawnObject)
}
