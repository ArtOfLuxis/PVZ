package serializers

import kotlinx.serialization.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*
import projectile.*
import registries.*

object ProjectileTypeSerializer : KSerializer<ProjectileType> {
    override val descriptor =
        PrimitiveSerialDescriptor("ProjectileType", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): ProjectileType {
        val id = decoder.decodeString()
        return ProjectileRegistry.get(id)
    }

    override fun serialize(encoder: Encoder, value: ProjectileType) {
        encoder.encodeString(value.id)
    }
}
