package trait

import game.objects.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import trait.events.*

open class Trait(
    private val fields: Map<String, KSerializer<*>>,
    val traitType: TraitType
) {
    open val values = hashMapOf<String, Any>()

    @Suppress("UNCHECKED_CAST")
    protected fun <T> get(key: String): T {
        return values[key] as T
    }

    open fun createInstance(parent: LawnObject): TraitInstance {
        error("Trait does not implement instance creation")
    }

    fun deserialize(json: JsonObject, jsonFormat: Json = Json): HashMap<String, Any> {
        if (!json.containsKey("id")) error("Missing required key: id")

        val result = HashMap<String, Any>()

        for ((key, serializer) in fields) {
            val element = json[key] ?: error("Missing required key: $key")
            val value = jsonFormat.decodeFromJsonElement(serializer, element)!!
            result[key] = value
        }

        result["id"] = json["id"]!!.jsonPrimitive.content
        return result
    }

    companion object {
        private val registry = HashMap<String, (JsonObject) -> Trait>()

        fun register(name: String, constructor: (JsonObject) -> Trait) {
            require(name !in registry) {
                "Trait $name is already registered"
            }
            registry[name] = constructor
        }

        fun from(name: String, json: JsonObject): Trait {
            val factory = registry[name] ?: error("Unknown trait: $name")
            return factory(json)
        }
    }

    override fun toString() = """
        ${this::class.simpleName}[
            ${values.entries.joinToString("\n") { "\t${it.key}=${it.value}" }}
        ]
    """.trimIndent()
}
