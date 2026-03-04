package tile.traits

import kotlinx.serialization.builtins.*
import kotlinx.serialization.json.*
import projectile.*
import trait.*

class EffectApplierTileTrait(
    jsonObject: JsonObject
) : Trait(hashMapOf(
        "effect" to String.serializer() // make a separate class soon
), TraitType.TILE) {
    private val effect: String

    init {
        val values = deserialize(jsonObject)
        effect = values["effect"] as String
    }

    override fun toString() = """
        EffectApplierTrait[
            effect=$effect
        ]
    """.trimIndent()
}
