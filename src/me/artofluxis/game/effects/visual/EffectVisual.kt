package me.artofluxis.game.effects.visual

import kotlinx.serialization.json.*
import korlibs.image.color.*

interface EffectVisual {
    companion object {
        fun from(type: String, json: JsonObject): EffectVisual {
            return when (type.lowercase()) {
                "tint" -> EffectVisualTint(
                    Colors[json["color"]!!.jsonPrimitive.content]
                        .withA(json["alpha"]!!.jsonPrimitive.int)
                )
                else -> error("Unknown effect visual '$type': $json")
            }
        }
    }
}
