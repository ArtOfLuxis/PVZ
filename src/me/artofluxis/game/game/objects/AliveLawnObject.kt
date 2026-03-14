package me.artofluxis.game.game.objects

import me.artofluxis.game.*
import me.artofluxis.game.effects.*

abstract class AliveLawnObject : DamageableLawnObject() {
    open val effects = hashMapOf<Effect, Timer>()
}
