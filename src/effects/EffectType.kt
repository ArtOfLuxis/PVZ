package effects

import effects.visual.*

class EffectType(
    val id: String,
    val visuals: HashSet<EffectVisual>,
    val effects: HashSet<Effect>
)
