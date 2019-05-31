package io.v47.tmdb.jackson.serialization

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer
import io.v47.tmdb.model.ImageSize

internal class ImageSizeSerializer : StdScalarSerializer<ImageSize>(ImageSize::class.java) {
    override fun serialize(value: ImageSize, gen: JsonGenerator, provider: SerializerProvider?) {
        gen.writeString(value.toString())
    }
}
