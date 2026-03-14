package me.artofluxis.game.mod.trait

import me.artofluxis.game.game.objects.TickableLawnObject

interface ObjectTraitInstance: TraitInstance {
    val parent: TickableLawnObject
}
