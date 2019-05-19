package io.v47.tmdb.jackson.deserialization

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer
import io.v47.tmdb.model.Height
import io.v47.tmdb.model.ImageSize
import io.v47.tmdb.model.Original
import io.v47.tmdb.model.Width

internal class ImageSizeDeserializer : StdScalarDeserializer<ImageSize>(ImageSize::class.java) {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): ImageSize {
        val rawString = p.valueAsString ?: throwParseException(p)

        val str = rawString.trim(' ', '"').toLowerCase()
        return if (str == "original")
            Original
        else {
            val firstChar = str.firstOrNull()
            if (firstChar != 'w' && firstChar != 'h')
                throwParseException(p)

            val intPart = str.substringAfter(firstChar, "")
            val actualInt = runCatching {
                intPart.toInt()
            }
                .onFailure { throwParseException(p, it) }
                .getOrThrow()

            if (firstChar == 'w')
                Width(actualInt)
            else
                Height(actualInt)
        }
    }

    private fun throwParseException(p: JsonParser, cause: Throwable? = null): Nothing =
        throw JsonParseException(
            p,
            "Expected image size string, got \"${p.currentToken().asString()}\"",
            cause
        )
}
