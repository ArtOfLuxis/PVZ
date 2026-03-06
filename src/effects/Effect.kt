package effects

import effects.visual.*

class Effect(
    val id: String,
    val visuals: HashSet<EffectVisual>,
    val effect1s: HashSet<EffectType>
)
