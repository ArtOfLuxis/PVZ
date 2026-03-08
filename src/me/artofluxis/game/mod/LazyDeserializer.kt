package me.artofluxis.game.mod

import kotlinx.serialization.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*

interface LazyDeserializer<T> : DeserializationStrategy<() -> T> {
    override val descriptor: SerialDescriptor

    override fun deserialize(decoder: Decoder): () -> T
}
