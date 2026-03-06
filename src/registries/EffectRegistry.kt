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

            val effects = hashSetOf<EffectType>()
            value.jsonObject["effects"]!!.jsonArray.forEach { obj ->
                val effectType = EffectModifierType.valueOf(
                    obj.jsonObject["type"]!!.jsonPrimitive.content.uppercase()
                )
                val effectValue = obj.jsonObject["value"]!!.jsonPrimitive.double
                val effectOperation = OperationType.valueOf(
                    obj.jsonObject["operation"]!!.jsonPrimitive.content
                )
                val effectOperationOrder = OperationOrder.valueOf(
                    obj.jsonObject["order"]!!.jsonPrimitive.content
                )
                val effect = EffectType(
                    effectType, effectValue, effectOperation,
                    effectOperationOrder
                )

                effects.add(effect)
            }

            effectTypes[id] = Effect(
                id, effectVisuals, effects
            )
        }
    }

    fun get(id: String): Effect =
        effectTypes[id] ?: error("Unknown effect id: $id")
}
