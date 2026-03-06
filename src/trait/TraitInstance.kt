package trait

import game.objects.*

abstract class TraitInstance(
    open val parent: TickableLawnObject,
    open val trait: Trait
)
