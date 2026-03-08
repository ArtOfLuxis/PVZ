package me.artofluxis.game.effects

import me.artofluxis.game.effects.visual.*

class Effect(
    val id: String,
    val visuals: HashSet<EffectVisual>,
    val effects: HashSet<EffectType>
) {
    override fun toString() = "${this::class.simpleName}[$id]"
}
