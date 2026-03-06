package registries

sealed interface Registry {
    suspend fun load()
}
