package me.artofluxis.game.game.types

data class SunDropperType(
    val id: String,
    val initialDelay: Double,
    val delay: Double,
    val sunValue: Int
) {
    companion object {
        val sunSizes = hashMapOf(
            250 to 3.0,
            100 to 2.0,
            50 to 1.25,
            25 to 0.75,
            5 to 0.5,
            1 to 0.25
        )
    }
}
