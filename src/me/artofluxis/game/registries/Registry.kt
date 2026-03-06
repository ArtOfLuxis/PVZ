package me.artofluxis.game.registries

sealed interface Registry {
    suspend fun load()
}
