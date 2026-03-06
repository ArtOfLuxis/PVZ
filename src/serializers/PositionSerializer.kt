package serializers

import Position
import kotlinx.serialization.*
import kotlinx.serialization.builtins.*
import kotlinx.serialization.encoding.*

object PositionSerializer : KSerializer<Position> {
    override val descriptor =
        ListSerializer(Double.serializer()).descriptor

    override fun deserialize(decoder: Decoder): Position {
        val list = decoder.decodeSerializableValue(ListSerializer(Double.serializer()))
        require(list.size == 2)
        return Position(list[0], list[1])
    }

    override fun serialize(encoder: Encoder, value: Position) {
        throw IllegalStateException("This is not supposed to be serialized")
    }
}
