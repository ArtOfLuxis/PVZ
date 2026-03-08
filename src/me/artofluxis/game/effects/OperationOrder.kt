package me.artofluxis.game.effects

class OperationOrder(
    val id: String,
    val order: Int
) {
    companion object {
        internal val registry = hashMapOf<String, OperationOrder>()

        fun register(id: String, order: Int) {
            val lowerId = id.lowercase()
            require(lowerId !in registry) {
                "Operation order $lowerId is already registered"
            }
            registry[lowerId] = OperationOrder(lowerId, order)
        }

        fun get(id: String) = registry[id.lowercase()]  ?: error("Unknown operation order id: $id")
    }
}
