package serializers

import game.hitbox.*
import kotlinx.serialization.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*
import projectile.*
import registries.*

object HitboxSerializer : KSerializer<Hitbox> {
    override val descriptor =
        PrimitiveSerialDescriptor("Hitbox", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Hitbox {
        val id = decoder.decodeString()
        return HitboxRegistry.get(id)
    }

    override fun serialize(encoder: Encoder, value: Hitbox) {
        throw IllegalStateException("This is not supposed to be serialized")
    }
}
