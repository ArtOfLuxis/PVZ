package registries

import effects.*
import effects.visual.*
import korlibs.io.file.std.*
import kotlinx.serialization.json.*

object EffectRegistry {
    val effectTypes = HashMap<String, EffectType>()

    suspend fun load() {
        val text = resourcesVfs["data/effects.json"].readString()
        val json = Json.parseToJsonElement(text).jsonArray

        for (value in json) {
            val id = value.jsonObject["id"]!!.jsonPrimitive.content

            val effectVisuals = hashSetOf<EffectVisual>()
            value.jsonObject["visuals"]!!.jsonArray.forEach { obj ->
                val visualType = obj.jsonObject["type"]!!.jsonPrimitive.content
                val effectVisual = EffectVisual.from(visualType, obj.jsonObject)

                effectVisuals.add(effectVisual)
            }

            val effects = hashSetOf<Effect>()
            value.jsonObject["effects"]!!.jsonArray.forEach { obj ->
                val effectType = EffectModifierType.valueOf(
                    obj.jsonObject["type"]!!.jsonPrimitive.content.uppercase()
                )
                val effectValue = obj.jsonObject["value"]!!.jsonPrimitive.double
                val effectModifier = ModifierType.valueOf(
                    obj.jsonObject["modifier"]!!.jsonPrimitive.content
                )
                val effect = Effect(effectType, effectValue, effectModifier)

                effects.add(effect)
            }

            effectTypes[id] = EffectType(
                id, effectVisuals, effects
            )
        }
    }

    fun get(id: String): EffectType =
        effectTypes[id] ?: error("Unknown effect type: $id")
}
