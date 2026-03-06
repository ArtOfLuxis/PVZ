package serializers

import effects.*
import kotlinx.serialization.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*
import registries.*

object EffectSerializer : KSerializer<Effect> {
    override val descriptor =
        PrimitiveSerialDescriptor("EffectType", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Effect {
        val id = decoder.decodeString()
        return EffectRegistry.get(id)
    }

    override fun serialize(encoder: Encoder, value: Effect) {
        throw IllegalStateException("This is not supposed to be serialized")
    }
}
