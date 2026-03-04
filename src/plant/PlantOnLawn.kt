package plant

import effects.*

class PlantOnLawn(
    val type: PlantType,
    val toughness: Int,
    val effects: List<EffectType>
)
