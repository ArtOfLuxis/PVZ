package registries

import effects.*
import effects.visual.*
import korlibs.io.file.std.*
import kotlinx.serialization.json.*

data object EffectRegistry : Registry {
    val effectTypes = HashMap<String, Effect>()

    override suspend fun load() {
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

            val effect1s = hashSetOf<EffectType>()
            value.jsonObject["effects"]!!.jsonArray.forEach { obj ->
                val effectType = EffectModifierType.valueOf(
                    obj.jsonObject["type"]!!.jsonPrimitive.content.uppercase()
                )
                val effectValue = obj.jsonObject["value"]!!.jsonPrimitive.double
                val effectModifier = ModifierType.valueOf(
                    obj.jsonObject["modifier"]!!.jsonPrimitive.content
                )
                val effect1 = EffectType(effectType, effectValue, effectModifier)

                effect1s.add(effect1)
            }

            effectTypes[id] = Effect(
                id, effectVisuals, effect1s
            )
        }
    }

    fun get(id: String): Effect =
        effectTypes[id] ?: error("Unknown effect id: $id")
}
