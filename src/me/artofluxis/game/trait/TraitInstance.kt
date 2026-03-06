package me.artofluxis.game.trait

import me.artofluxis.game.game.objects.TickableLawnObject

abstract class TraitInstance(
    open val parent: TickableLawnObject,
    open val trait: Trait
)
