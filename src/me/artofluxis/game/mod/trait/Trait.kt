package me.artofluxis.game.mod.trait

import kotlinx.coroutines.*
import kotlinx.serialization.json.*
import me.artofluxis.game.*
import me.artofluxis.game.game.objects.LocationalLawnObject
import me.artofluxis.game.mod.*

open class Trait(
    private val fields: Map<String, LazyDeserializer<*>>,
    val traitType: TraitType,
    jsonObject: JsonObject
) {
    private val values = deserialize(jsonObject)
    open val cachedValues = hashMapOf<String, Any>()

    @Suppress("UNCHECKED_CAST")
    protected fun <T> get(key: String): T {
        val cached = cachedValues[key]
        if (cached != null) return cached as T

        val value = values[key]?.invoke()
            ?: error("Value '$key' not found")

        // weak reference
        cachedValues[key] = value
        return value as T
    }

    open fun createInstance(parent: LocationalLawnObject): TraitInstance {
        error("Trait does not implement instance creation")
    }

    fun deserialize(json: JsonObject, jsonFormat: Json = Json): HashMap<String, () -> Any?> {
        val result = HashMap<String, () -> Any?>()

        for ((key, serializer) in fields) {
            val element = json[key] ?: error("Missing required key: $key")
            val value = jsonFormat.decodeFromJsonElement(serializer, element)
            result[key] = value
        }

        return result
    }

    companion object {
        internal val registry = HashMap<String, (JsonObject) -> Trait>()

        fun register(name: String, constructor: (JsonObject) -> Trait) {
            val lowerName = name.lowercase()
            require(lowerName !in registry) {
                "Trait $name is already registered"
            }
            registry[lowerName] = constructor
        }

        fun from(name: String, json: JsonObject): Trait {
            val lowerName = name.lowercase()
            val factory = registry[lowerName] ?: error("Unknown trait: $name")

            try {
                factory(json)
            } catch (e: Exception) {
                originalSceneContainer.launch {
                    handleException(e, "Encountered Exception when initializing trait $name")
                }
            }

            return factory(json)
        }
    }

    override fun toString() = """
        ${this::class.simpleName}[${cachedValues.entries.joinToString(", ") { "\t${it.key}=${it.value}" }}]
    """.trimIndent()
}
