package me.artofluxis.game.registries

import me.artofluxis.game.dataFolder
import kotlinx.serialization.json.*
import me.artofluxis.game.effects.*
import me.artofluxis.game.effects.visual.*
import java.io.*

data object EffectRegistry : Registry {
    val effectTypes = HashMap<String, Effect>()

    override suspend fun load() {
        val text = File(dataFolder, "effects.json").readText()
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
                val effectType = EffectModifierType.get(
                    obj.jsonObject["type"]!!.jsonPrimitive.content.lowercase()
                )
                val effectValue = obj.jsonObject["value"]!!.jsonPrimitive.double
                val effectOperation = OperationType.get(
                    obj.jsonObject["operation"]!!.jsonPrimitive.content.lowercase()
                )
                val effectOperationOrder = OperationOrder.get(
                    obj.jsonObject["order"]!!.jsonPrimitive.content.lowercase()
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
