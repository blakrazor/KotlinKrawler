package com.achanr.kotlinkrawler.models

import kotlinx.serialization.*

@Serializable(Difficulty.DifficultySerializer::class)
enum class Difficulty(val value: Int) {
    VeryEasy(0),
    Easy(1),
    Medium(2),
    Hard(3),
    VeryHard(4);

    companion object {
        fun fromInt(value: Int) = values().first { it.value == value }
    }

    @Serializer(Difficulty::class)
    object DifficultySerializer {
        override val descriptor: SerialDescriptor
            get() = PrimitiveDescriptor(
                "com.achanr.kotlinkrawler.models.Difficulty.DifficultySerializer",
                PrimitiveKind.INT
            )

        override fun deserialize(decoder: Decoder): Difficulty {
            return fromInt(decoder.decodeInt())
        }

        override fun serialize(encoder: Encoder, obj: Difficulty) {
            encoder.encodeInt(obj.value)
        }
    }
}