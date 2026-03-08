package me.artofluxis.game.effects

class EffectModifierType(
    val id: String
) {
    companion object {
        internal val registry = hashMapOf<String, EffectModifierType>()

        fun register(id: String) {
            val lowerId = id.lowercase()
            require(lowerId !in registry) {
                "Effect $lowerId is already registered"
            }
            registry[lowerId] = EffectModifierType(lowerId)
        }

        fun get(id: String) = registry[id.lowercase()]  ?: error("Unknown effect modifier type: $id")
    }
}
