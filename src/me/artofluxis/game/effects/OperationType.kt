package me.artofluxis.game.effects

class OperationType(
    val id: String,
    val operation: (Double, Double) -> Double
) {
    companion object {
        internal val registry = hashMapOf<String, OperationType>()

        fun register(id: String, operation: (Double, Double) -> Double) {
            val lowerId = id.lowercase()
            require(lowerId !in registry) {
                "Operation type $lowerId is already registered"
            }
            registry[lowerId] = OperationType(lowerId, operation)
        }

        fun get(id: String) = registry[id.lowercase()]  ?: error("Unknown operation type: $id")
    }
}
