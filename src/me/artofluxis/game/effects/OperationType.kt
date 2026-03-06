package me.artofluxis.game.effects

enum class OperationType(
    val operation: (Double, Double) -> Double
) {
    ADDITION({ f, s -> f + s }), MULTIPLICATION({ f, s -> f * s })
}
