package me.artofluxis.game.mod.trait

interface TraitInstance {
    val trait: Trait
    /** Supposed to be called to clean-up coroutines etc. etc. on trait destruction */
    fun destroy() {}
}
